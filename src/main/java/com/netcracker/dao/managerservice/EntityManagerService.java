package com.netcracker.dao.managerservice;

import com.netcracker.dao.Entity;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.dao.manager.EntityManager;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.error.ErrorMessage;
import com.netcracker.error.exceptions.EntityManagerException;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.UsersProfileConstant;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

public class EntityManagerService {
    @Autowired
    private Logger logger;
    private EntityManager manager;

    @Autowired
    public EntityManagerService(EntityManager manager) {
        this.manager = manager;
    }

    public int getAllCount(BigInteger objectTypeId) {
        return manager.getAllCount(objectTypeId);
    }

    public int getBySqlCount(String sqlQuery) {
        return manager.getBySqlCount(sqlQuery);
    }

    @Transactional
    public <T extends BaseEntity> T create(T baseEntity) {
        baseEntity.setObjectId(manager.getNextObjId());
        Entity entity = new Converter().convertToEntity(baseEntity);
        manager.create(entity);
        return baseEntity;
    }

    public void dropRef(int attr_id, BigInteger obj, int flag) {
        manager.dropRef(attr_id, obj, flag);
    }

    public void dropRefDual(int attr_id, BigInteger ref, BigInteger obj) {
        manager.dropRefDual(attr_id, ref, obj);
    }

    public void insertObjref(int attr_id, BigInteger ref, BigInteger obj) {
        manager.insertObjref(attr_id, ref, obj);
    }

    public <T extends BaseEntity> BigInteger update(T baseEntity) {
        if (baseEntity.getObjectId() == null) {
            T temp = create(baseEntity);
            return temp.getObjectId();
        }
        manager.update(new Converter().convertToEntity(baseEntity));
        return baseEntity.getObjectId();
    }

    public void delete(BigInteger objectId, int forceDelete) {
        manager.delete(objectId, forceDelete);
    }

    public <T extends BaseEntity> T getById(BigInteger objectId, Class baseEntityClass) {
        Entity entity = manager.getById(objectId);
        return (T) new Converter().convertToBaseEntity(entity, baseEntityClass);
    }

    private <T extends BaseEntity> T getByIdRef(BigInteger objectId, Class baseEntityClass) {
        Entity entity = manager.getByIdRef(objectId);
        return (T) new Converter().convertToBaseEntity(entity, baseEntityClass);
    }

    public <T extends BaseEntity> List<T> getAll(BigInteger objectTypeId, Class<T> baseEntityClass, QueryDescriptor queryDescriptor) {
        List<Entity> entities = manager.getAll(objectTypeId, queryDescriptor);
        List<T> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            T baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    public <T extends BaseEntity> List<T> getObjectsBySQL(String sqlQuery, Class<T> baseEntityClass, QueryDescriptor queryDescriptor) {
        List<Entity> entities = manager.getBySQL(sqlQuery, queryDescriptor);
        List<T> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            T baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    private class Converter {

        private final Date EMPTY_DATE = new Date(-1);
        private final Timestamp EMPTY_TIMESTAMP = new Timestamp(-1);
        private final String EMPTY_STRING = "-1";
        private final BigInteger EMPTY_INTEGER = BigInteger.valueOf(-1);

        private Entity convertToEntity(BaseEntity baseEntity) {
            Class baseEntityClass = baseEntity.getClass();
            Entity entity = new Entity();
            Map<Pair<BigInteger, Integer>, Object> attributes = new HashMap<>();
            Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references = new HashMap<>();

            if (baseEntityClass.isAnnotationPresent(ObjectType.class)) {
                ObjectType objectType = (ObjectType) baseEntityClass.getAnnotation(ObjectType.class);
                int objectTypeId = objectType.value();
                entity.setObjectTypeId(BigInteger.valueOf(objectTypeId));
            }
            getFieldsFromParents(baseEntity, attributes, references);
            Field[] fields = baseEntityClass.getDeclaredFields();
            getAllFields(fields, baseEntity, baseEntityClass, attributes, references);

            entity.setName(baseEntity.getName());
            entity.setObjectId(baseEntity.getObjectId());
            entity.setDescription(baseEntity.getDescription());
            entity.setParentId(baseEntity.getParentId());

            entity.setAttributes(attributes);
            entity.setReferences(references);
            return entity;
        }

        private void getAllFields(Field[] fields, BaseEntity baseEntity, Class<?> baseEntityClass,
                                  Map<Pair<BigInteger, Integer>, Object> attributes,
                                  Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references) {
            BigInteger attrId;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                    attrId = BigInteger.valueOf(attributeAnnotation.value());
                    putAttribute(attributes, attrId, baseEntity, baseEntityClass, field);
                }
                if (field.isAnnotationPresent(Boolean.class)) {
                    Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                    attrId = BigInteger.valueOf(booleanAnnotation.value());
                    String yesno = String.valueOf(booleanAnnotation.yesno());
                    putBoolean(attributes, attrId, yesno, baseEntity, baseEntityClass, field);
                }
                if (field.isAnnotationPresent(Reference.class)) {
                    Reference referenceAnnotation = field.getAnnotation(Reference.class);
                    attrId = BigInteger.valueOf(referenceAnnotation.value());
                    Integer isParentChildRef = referenceAnnotation.isParentChild();
                    putReference(references, attrId, isParentChildRef, baseEntity, baseEntityClass, field);
                }
            }
        }

        private void putBoolean(Map<Pair<BigInteger, Integer>, Object> attributes, BigInteger attrId, String yesno,
                                BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object fieldValue = getValueFromField(baseEntity, baseEntityClass, field);
            if (List.class.isAssignableFrom(fieldValue.getClass())) {
                List<java.lang.Boolean> booleans = (List<java.lang.Boolean>) getValueFromField(baseEntity, baseEntityClass, field);
                Integer seq_no = manager.getNextSeqNoObjAttributes(baseEntity.getObjectId(), attrId);
                if (booleans != null) {
                    for (int i = 0; i < booleans.size(); i++) {
                        java.lang.Boolean element = booleans.get(i);
                        if (element) {
                            attributes.put(new Pair<>(attrId, seq_no + i), yesno);
                        } else {
                            attributes.put(new Pair<>(attrId, seq_no + i), "otherValue");
                        }
                    }
                }
            } else {
                java.lang.Boolean element = (java.lang.Boolean) fieldValue;
                if (element) {
                    attributes.put(new Pair<>(attrId, 0), yesno);
                } else {
                    attributes.put(new Pair<>(attrId, 0), "otherValue");
                }
            }
        }

        private void putAttribute(Map<Pair<BigInteger, Integer>, Object> attributes, BigInteger attrId, BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object fieldValue = getValueFromField(baseEntity, baseEntityClass, field);
            if (fieldValue == null) {
                if (field.getType() == String.class) {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_STRING);
                } else if (field.getType() == Date.class) {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_DATE);
                } else if (field.getType() == Timestamp.class) {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_TIMESTAMP);
                } else {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_STRING);
                }
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                }
                if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List tempAttributes = (List) fieldValue;
                    Integer seq_no = manager.getNextSeqNoObjAttributes(baseEntity.getObjectId(), attrId);
                    for (int i = 0; i < tempAttributes.size(); i++) {
                        Object attribute = tempAttributes.get(i);
                        if (Timestamp.class.isAssignableFrom(fieldValue.getClass())) {
                            attributes.put(new Pair<>(attrId, seq_no + i), attribute);
                        } else if (Date.class.isAssignableFrom(attribute.getClass())) {
                            attributes.put(new Pair<>(attrId, seq_no + i), attribute.toString());
                        } else {
                            attributes.put(new Pair<>(attrId, seq_no + i), attribute.toString());
                        }
                    }
                } else {
                    if (Timestamp.class.isAssignableFrom(fieldValue.getClass())) {
                        attributes.put(new Pair<>(attrId, 0), fieldValue);
                    } else if (Date.class.isAssignableFrom(fieldValue.getClass())) {
                        attributes.put(new Pair<>(attrId, 0), fieldValue);
                    } else {
                        attributes.put(new Pair<>(attrId, 0), fieldValue.toString());
                    }
                }
            }
        }

        private Integer getSeqNo(BigInteger obj_id, BigInteger ref_id, BigInteger attr_type_id) {
            Integer seq_no;
            if (attr_type_id.intValue() == UsersProfileConstant.USER_PROFILE) {
                seq_no = 0;
            } else {
                seq_no = manager.getNextSeqNoObjRef(obj_id, ref_id, attr_type_id);
            }
            return seq_no;
        }

        private void putReference(Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> references, BigInteger attrId, Integer isParentChildRef,
                                  BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object fieldValue = getValueFromField(baseEntity, baseEntityClass, field);
            BigInteger curObjId = baseEntity.getObjectId();
            Pair<BigInteger, BigInteger> objIdRef;
            if (fieldValue == null) {
                if (isParentChildRef == 1) {
                    objIdRef = new Pair<>(EMPTY_INTEGER, curObjId);
                } else {
                    objIdRef = new Pair<>(curObjId, EMPTY_INTEGER);
                }

                references.put(new Pair<>(attrId, getSeqNo(objIdRef.getKey(), objIdRef.getValue(), attrId)), objIdRef);
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                }
                if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List<BaseEntity> tempReferences = (List<BaseEntity>) fieldValue;

                    for (int i = 0; i < tempReferences.size(); i++) {
                        BaseEntity reference = tempReferences.get(i);
                        if (isParentChildRef == 1) {
                            objIdRef = new Pair<>(reference != null ? reference.getObjectId() : EMPTY_INTEGER, curObjId);
                        } else {
                            objIdRef = new Pair<>(curObjId, reference != null ? reference.getObjectId() : EMPTY_INTEGER);
                        }
                        references.put(new Pair<>(attrId, getSeqNo(objIdRef.getKey(), objIdRef.getValue(), attrId) + i), objIdRef);
                    }
                } else {
                    if (isParentChildRef == 1) {
                        objIdRef = new Pair<>(((BaseEntity) fieldValue).getObjectId(), curObjId);
                    } else {
                        objIdRef = new Pair<>(curObjId, ((BaseEntity) fieldValue).getObjectId());
                    }
                    references.put(new Pair<>(attrId, getSeqNo(objIdRef.getKey(), objIdRef.getValue(), attrId)), objIdRef);
                }
            }
        }

        private Object getValueFromField(BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object value;
            PropertyDescriptor property;
            try {
                property = new PropertyDescriptor(field.getName(), baseEntityClass);
                Method getMethod = property.getReadMethod();
                value = getMethod.invoke(baseEntity);
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                logger.throwing(EntityManagerService.class.getName(), "getValueFromField", e);
                throw new EntityManagerException(ErrorMessage.ERROR_UNKNOWN, e);
            }
            return value;
        }

        private <T extends BaseEntity> T convertToBaseEntity(Entity entity, Class clazz) throws EntityManagerException {
            if (clazz == BaseEntity.class) {
                Throwable ex = new IllegalArgumentException("You can't create object with BaseEntity type");
                logger.throwing(EntityManager.class.getName(), "convertToBaseEntity", ex);
                throw new EntityManagerException(ErrorMessage.ERROR_UNKNOWN, ex);
            }
            T baseEntity;
            try {
                baseEntity = (T) clazz.newInstance();
                baseEntity.setName(entity.getName());
                baseEntity.setObjectId(entity.getObjectId());
                baseEntity.setDescription(entity.getDescription());
                baseEntity.setParentId(entity.getParentId());

                Map<Pair<BigInteger, Integer>, Object> attrMap = entity.getAttributes();
                Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> refMap = entity.getReferences();
                if (!attrMap.isEmpty() || !refMap.isEmpty()) {
                    setFieldsInParents(baseEntity, attrMap, refMap);
                    Field[] fields = clazz.getDeclaredFields();
                    setAllFields(fields, baseEntity, clazz, attrMap, refMap);
                }
                return baseEntity;
            } catch (InstantiationException | IllegalAccessException e) {
                logger.throwing(EntityManager.class.getName(), "convertToBaseEntity", e);
                throw new EntityManagerException(ErrorMessage.ERROR_UNKNOWN, e);
            }
        }

        private void setAllFields(Field[] fields, BaseEntity baseEntity, Class<?> baseEntityClass,
                                  Map<Pair<BigInteger, Integer>, Object> attrMap,
                                  Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> refMap) {
            BigInteger attrId;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                    attrId = BigInteger.valueOf(attributeAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                    setValue(field, baseEntity, baseEntityClass, pairs, attrMap);
                } else if (field.isAnnotationPresent(Boolean.class)) {
                    Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                    attrId = BigInteger.valueOf(booleanAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                    String yesno = String.valueOf(booleanAnnotation.yesno());
                    setBoolean(field, baseEntity, baseEntityClass, pairs, attrMap, yesno);
                } else if (field.isAnnotationPresent(Reference.class)) {
                    Reference referenceAnnotation = field.getAnnotation(Reference.class);
                    attrId = BigInteger.valueOf(referenceAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, refMap.keySet());
                    setRef(field, baseEntity, baseEntityClass, pairs, refMap, baseEntity.getObjectId());
                }
            }
        }

        private void setRef(Field field, BaseEntity baseEntity, Class<?> baseEntityClass,
                            List<Pair<BigInteger, Integer>> pairs,
                            Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> refMap,
                            BigInteger outerEntityId) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType) || Set.class.isAssignableFrom(fieldType)) {
                Object[] values = new Object[pairs.size()];
                int counter = 0;
                for (Pair<BigInteger, Integer> pair : pairs) {
                    Pair<BigInteger, BigInteger> objIdRef = refMap.get(pair);
                    BigInteger attribute;
                    if (!outerEntityId.equals(objIdRef.getKey())) {
                        attribute = objIdRef.getKey();
                    } else {
                        attribute = objIdRef.getValue();
                    }
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                    values[counter] = attribute != null ? getByIdRef(attribute, listClass) : null;
                    counter++;
                }
                value = Arrays.asList(values);
                if (Set.class.isAssignableFrom(fieldType)) {
                    value = new HashSet<>(Arrays.asList(values));
                }
            } else {
                if (!pairs.isEmpty()) {
                    Pair<BigInteger, BigInteger> objIdRef = refMap.get(pairs.get(0));
                    BigInteger attribute;
                    if (!outerEntityId.equals(objIdRef.getKey())) {
                        attribute = objIdRef.getKey();
                    } else {
                        attribute = objIdRef.getValue();
                    }
                    value = attribute != null ? getByIdRef(attribute, fieldType) : null;
                }
            }
            setValueIntoField(field, baseEntity, baseEntityClass, value);
        }

        private void setValue(Field field, BaseEntity baseEntity, Class<?> baseEntityClass,
                              List<Pair<BigInteger, Integer>> pairs, Map<Pair<BigInteger, Integer>, Object> attrMap) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType)) {
                Object[] values = new Object[pairs.size()];
                int counter = 0;
                for (Pair<BigInteger, Integer> pair : pairs) {
                    Object attribute = attrMap.get(pair);
                    if (attribute != null) {
                        if (Timestamp.class.isAssignableFrom(attribute.getClass())) {
                            values[counter] = Timestamp.valueOf(String.valueOf(attribute)).getTime();
                        } else if (Date.class.isAssignableFrom(attribute.getClass())) {
                            values[counter] = new Date(Timestamp.valueOf(String.valueOf(attribute)).getTime());
                        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
                            values[counter] = new BigDecimal((String) attribute);
                        } else {
                            values[counter] = attribute;
                        }
                        value = Arrays.asList(values);
                    }
                    counter++;
                }
            } else {
                if (!pairs.isEmpty()) {
                    Object attribute = attrMap.get(pairs.get(0));
                    if (Timestamp.class.isAssignableFrom(fieldType)) {
                        value = attribute != null ? Timestamp.valueOf(String.valueOf(attribute)) : null;
                    } else if (Date.class.isAssignableFrom(fieldType)) {
                        value = attribute != null ? new Date(((Timestamp) attribute).getTime()) : null;
                    } else if (String.class.isAssignableFrom(fieldType)) {
                        value = attribute;
                    } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
                        value = attribute != null ? new BigDecimal((String) attribute) : null;
                    } else {
                        if (attribute != null) {
                            try {
                                Method m = fieldType.getMethod("valueOf", String.class);
                                value = m.invoke(fieldType, attribute);
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                logger.throwing(EntityManager.class.getName(), "setValue", e);
                                throw new EntityManagerException(ErrorMessage.ERROR_UNKNOWN, e);
                            }
                        }
                    }
                }
            }
            setValueIntoField(field, baseEntity, baseEntityClass, value);
        }

        private void setBoolean(Field field, BaseEntity baseEntity, Class<?> baseEntityClass, List<Pair<BigInteger, Integer>> pairs,
                                Map<Pair<BigInteger, Integer>, Object> attrMap, String yesno) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType)) {
                java.lang.Boolean[] values = new java.lang.Boolean[pairs.size()];
                int counter = 0;
                for (Pair<BigInteger, Integer> pair : pairs) {
                    String attribute = (String) attrMap.get(pair);
                    values[counter] = yesno.equals(attribute);
                    counter++;
                }
                value = Arrays.asList(values);
            } else {
                if (!pairs.isEmpty()) {
                    String attribute = (String) attrMap.get(pairs.get(0));
                    value = yesno.equals(attribute);
                }
            }
            setValueIntoField(field, baseEntity, baseEntityClass, value);
        }

        private void setValueIntoField(Field field, BaseEntity baseEntity, Class<?> baseEntityClass, Object value) {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), baseEntityClass);
                Method setMethod = propertyDescriptor.getWriteMethod();
                if (setMethod != null) {
                    setMethod.invoke(baseEntity, value);
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                logger.throwing(EntityManager.class.getName(), "setValueIntoField", e);
                throw new EntityManagerException(ErrorMessage.ERROR_UNKNOWN, e);
            }
        }

        private List<Class<?>> getAllClassInHierarchy(Class<?> objectClass) {
            List<Class<?>> classes1 = new ArrayList<>();
            Class<?> superClass = objectClass.getSuperclass();
            if (superClass.getSuperclass() != null) {
                List<Class<?>> classes = getAllClassInHierarchy(superClass);
                classes1.addAll(classes);
            }
            classes1.add(superClass);
            return classes1;
        }

        private void getFieldsFromParents(BaseEntity baseEntity, Map<Pair<BigInteger, Integer>, Object> attrMap,
                                          Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> refMap) {
            List<Class<?>> parents = getAllClassInHierarchy(baseEntity.getClass());
            Collections.reverse(parents);
            Iterator<Class<?>> iterator = parents.iterator();
            while (iterator.hasNext()) {
                Class<?> clazz = iterator.next();
                if (clazz != BaseEntity.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    getAllFields(fields, baseEntity, clazz, attrMap, refMap);
                } else {
                    break;
                }
            }
        }

        private void setFieldsInParents(BaseEntity baseEntity, Map<Pair<BigInteger, Integer>, Object> attrMap,
                                        Map<Pair<BigInteger, Integer>, Pair<BigInteger, BigInteger>> refMap) {
            List<Class<?>> parents = getAllClassInHierarchy(baseEntity.getClass());
            Collections.reverse(parents);
            Iterator<Class<?>> iterator = parents.iterator();
            while (iterator.hasNext()) {
                Class<?> clazz = iterator.next();
                if (clazz != BaseEntity.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    setAllFields(fields, baseEntity, clazz, attrMap, refMap);
                } else {
                    break;
                }
            }
        }

        private List<Pair<BigInteger, Integer>> getAllPairs(BigInteger attrId, Set<Pair<BigInteger, Integer>> keyPairs) {
            List<Pair<BigInteger, Integer>> pairs = new ArrayList<>();
            for (Pair<BigInteger, Integer> pair : keyPairs) {
                if (pair.getKey().equals(attrId)) {
                    pairs.add(pair);
                }
            }
            return pairs;
        }
    }
}