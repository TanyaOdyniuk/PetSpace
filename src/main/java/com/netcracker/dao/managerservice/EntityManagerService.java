package com.netcracker.dao.managerservice;

import com.netcracker.dao.Entity;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.dao.manager.EntityManager;
import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.model.BaseEntity;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class EntityManagerService {

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
        Entity entity = manager.create(new Converter().convertToEntity(baseEntity));
        baseEntity.setObjectId(entity.getObjectId());
        return baseEntity;
    }

    public <T extends BaseEntity> void update(T baseEntity) {
        if (baseEntity.getObjectId() == null) {
            create(baseEntity);
        }
        manager.update(new Converter().convertToEntity(baseEntity));
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

    public <T extends BaseEntity> List<T> getAll(BigInteger objectTypeId, Class<T> baseEntityClass, QueryDescriptor queryDescriptor/*, boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc*/) {
        List<Entity> entities = manager.getAll(objectTypeId, queryDescriptor);
        List<T> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            T baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    public <T extends BaseEntity> List<T> getObjectsBySQL(String sqlQuery, Class<T> baseEntityClass, QueryDescriptor queryDescriptor /*boolean isPaging, Pair<Integer, Integer> pagingDesc, Map<String, String> sortingDesc*/) {
        List<Entity> entities = manager.getBySQL(sqlQuery, queryDescriptor);
        List<T> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            T baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    class Converter {

        private final Date EMPTY_DATE = new Date(-1);
        private final Timestamp EMPTY_TIMESTAMP = new Timestamp(-1);
        private final String EMPTY_STRING = "-1";
        private final BigInteger EMPTY_INTEGER = BigInteger.valueOf(-1);

        private Entity convertToEntity(BaseEntity baseEntity) {
            Class baseEntityClass = baseEntity.getClass();
            Entity entity = new Entity();
            Map<Pair<BigInteger, Integer>, Object> attributes = new HashMap<>();
            Map<Pair<BigInteger, Integer>, BigInteger> references = new HashMap<>();

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
        private void getAllFields(Field[] fields, BaseEntity baseEntity, Class<?> baseEntityClass, Map<Pair<BigInteger, Integer>, Object> attributes,
                                  Map<Pair<BigInteger, Integer>, BigInteger> references){
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
                    putReference(references, attrId, baseEntity, baseEntityClass, field);
                }
            }
        }
        private void putBoolean(Map<Pair<BigInteger, Integer>, Object> attributes, BigInteger attrId, String yesno,
                                BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object fieldValue = getValueFromField(baseEntity, baseEntityClass, field);
            if (List.class.isAssignableFrom(fieldValue.getClass())) {
                List<java.lang.Boolean> booleans = (List<java.lang.Boolean>) getValueFromField(baseEntity, baseEntityClass, field);
                if (booleans != null) {
                    for (int i = 0; i < booleans.size(); i++) {
                        java.lang.Boolean element = booleans.get(i);
                        if (element) {
                            attributes.put(new Pair<>(attrId, i + 1), yesno);
                        } else {
                            attributes.put(new Pair<>(attrId, i + 1), "otherValue");
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
                } else {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_TIMESTAMP);
                }
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                }
                if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List tempAttributes = (List) fieldValue;
                    if (tempAttributes != null) {
                        for (int i = 0; i < tempAttributes.size(); i++) {
                            Object attribute = tempAttributes.get(i);
                            if (Timestamp.class.isAssignableFrom(fieldValue.getClass())) {
                                attributes.put(new Pair<>(attrId, i + 1), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(attribute));
                            } else if (Date.class.isAssignableFrom(attribute.getClass())) {
                                attributes.put(new Pair<>(attrId, i + 1), attribute.toString());
                            } else {
                                attributes.put(new Pair<>(attrId, i + 1), attribute.toString());
                            }
                        }
                    }
                } else {
                    if (Timestamp.class.isAssignableFrom(fieldValue.getClass())) {
                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fieldValue);
                        attributes.put(new Pair<>(attrId, 0), formattedDate);
                    } else if (Date.class.isAssignableFrom(fieldValue.getClass())) {
                        attributes.put(new Pair<>(attrId, 0), fieldValue);
                    } else {
                        attributes.put(new Pair<>(attrId, 0), fieldValue.toString());
                    }
                }
            }
        }

        private void putReference(Map<Pair<BigInteger, Integer>, BigInteger> references, BigInteger attrId, BaseEntity baseEntity, Class<?> baseEntityClass, Field field) {
            Object fieldValue = getValueFromField(baseEntity, baseEntityClass, field);
            if (fieldValue == null) {
                references.put(new Pair<>(attrId, 0), EMPTY_INTEGER);
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                }
                if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List<BaseEntity> tempReferences = (List<BaseEntity>) fieldValue;
                    for (int i = 0; i < tempReferences.size(); i++) {
                        BaseEntity reference = tempReferences.get(i);
                        references.put(new Pair<>(attrId, i + 1), reference != null ? reference.getObjectId() : EMPTY_INTEGER);
                    }
                } else {
                    references.put(new Pair<>(attrId, 0), ((BaseEntity) fieldValue).getObjectId());
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
            } catch (IntrospectionException e) {
                throw new RuntimeException("Unable to find the field");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("The field doesn't have public get method.");
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return value;
        }

        private <T extends BaseEntity> T convertToBaseEntity(Entity entity, Class clazz) {
            if (clazz == BaseEntity.class)
                throw new IllegalArgumentException("You can't create object with BaseEntity type");
            T baseEntity = null;
            try {
                baseEntity = (T) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            baseEntity.setName(entity.getName());
            baseEntity.setObjectId(entity.getObjectId());
            baseEntity.setDescription(entity.getDescription());
            baseEntity.setParentId(entity.getParentId());

            Map<Pair<BigInteger, Integer>, Object> attrMap = entity.getAttributes();
            Map<Pair<BigInteger, Integer>, BigInteger> refMap = entity.getReferences();
            if (!attrMap.isEmpty() || !refMap.isEmpty()) {
                setFieldsInParents(baseEntity, attrMap, refMap);
                Field[] fields = clazz.getDeclaredFields();
                setAllFields(fields, baseEntity, clazz, attrMap, refMap);
            }
            return baseEntity;
        }

        private void setAllFields(Field[] fields, BaseEntity baseEntity, Class<?> baseEntityClass, Map<Pair<BigInteger, Integer>, Object> attrMap,
                                  Map<Pair<BigInteger, Integer>, BigInteger> refMap){
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
                            List<Pair<BigInteger, Integer>> pairs, Map<Pair<BigInteger, Integer>, BigInteger> refMap, BigInteger outerEntityId) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType) || Set.class.isAssignableFrom(fieldType)) {
                Object[] values = new Object[getMaxSeqNo(pairs)];
                for (Pair<BigInteger, Integer> pair : pairs) {
                    BigInteger attribute = refMap.get(pair);
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                    if (pair.getValue() != 0) {
                        values[pair.getValue() - 1] = attribute != null ? getByIdRef(attribute, listClass) : null;
                    } else {
                        values[0] = attribute != null ? getByIdRef(attribute, listClass) : null;
                    }
                }
                value = Arrays.asList(values);
                if (Set.class.isAssignableFrom(fieldType)) {
                    value = new HashSet<>(Arrays.asList(values));
                }
            } else {
                if (!pairs.isEmpty()) {
                    BigInteger attribute = refMap.get(pairs.get(0));
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
                Object[] values = new Object[getMaxSeqNo(pairs)];
                for (Pair<BigInteger, Integer> pair : pairs) {
                    Object attribute = attrMap.get(pair);
                    if (attribute != null) {
                        if (Timestamp.class.isAssignableFrom(attribute.getClass())) {
                            values[pair.getValue() - 1] = Timestamp.valueOf(String.valueOf(attribute)).getTime();
                        } else if (Date.class.isAssignableFrom(attribute.getClass())) {
                            values[pair.getValue() - 1] = new Date(Timestamp.valueOf(String.valueOf(attribute)).getTime());
                        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
                            values[pair.getValue() - 1] = new BigDecimal((String) attribute);
                        } else {
                            values[pair.getValue() - 1] = attribute;
                        }
                        value = Arrays.asList(values);
                    }
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
                                e.printStackTrace();
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
                java.lang.Boolean[] values = new java.lang.Boolean[getMaxSeqNo(pairs)];
                for (Pair<BigInteger, Integer> pair : pairs) {
                    String attribute = (String) attrMap.get(pair);
                    values[pair.getValue()] = yesno.equals(attribute);
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
            } catch (IntrospectionException e) {
                throw new RuntimeException("Unable to find the field");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("The field doesn't have public set method.");
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
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

        private void getFieldsFromParents(BaseEntity baseEntity, Map<Pair<BigInteger, Integer>, Object> attrMap, Map<Pair<BigInteger, Integer>, BigInteger> refMap){
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
        private void setFieldsInParents(BaseEntity baseEntity, Map<Pair<BigInteger, Integer>, Object> attrMap, Map<Pair<BigInteger, Integer>, BigInteger> refMap) {
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

        private Integer getMaxSeqNo(List<Pair<BigInteger, Integer>> pairs) {
            Integer max = 1;
            for (Pair<BigInteger, Integer> pair : pairs) {
                if (pair.getValue() > max) {
                    max = pair.getValue();
                }
            }
            return max;
        }
    }
}