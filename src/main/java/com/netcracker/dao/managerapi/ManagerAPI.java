package com.netcracker.dao.managerapi;

import com.netcracker.dao.Entity;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.dao.manager.EntityManager;
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
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

public class ManagerAPI {

    private EntityManager manager;

    @Autowired
    public ManagerAPI(EntityManager manager) {
        this.manager = manager;
    }
    @Transactional
    public BaseEntity create(BaseEntity baseEntity) {
        Entity entity = manager.create(new Converter().convertToEntity(baseEntity));
        baseEntity.setObjectId(entity.getObjectId());
        return baseEntity;
    }

    public void update(BaseEntity baseEntity) {
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

    private  <T extends BaseEntity> T getByIdRef(BigInteger objectId, Class baseEntityClass) {
        Entity entity = manager.getByIdRef(objectId);
        return (T) new Converter().convertToBaseEntity(entity, baseEntityClass);

    }
    public List<BaseEntity> getAll(BigInteger objectTypeId, Class baseEntityClass) {
        List<Entity> entities = manager.getAll(objectTypeId);
        List<BaseEntity> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            BaseEntity baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    public List<BaseEntity> getObjectsBySQL(String sqlQuery, Class baseEntityClass) {
        List<Entity> entities = manager.getBySQL(sqlQuery);
        List<BaseEntity> baseEntities = new ArrayList<>();
        for (Entity entity : entities) {
            BaseEntity baseEntity = new Converter().convertToBaseEntity(entity, baseEntityClass);
            baseEntities.add(baseEntity);
        }
        return baseEntities;
    }

    class Converter {

        private final Date EMPTY_DATE = new Date(-1);
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
            Field[] fields = baseEntityClass.getDeclaredFields();
            BigInteger attrId;

            for (Field field : fields) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                    attrId = BigInteger.valueOf(attributeAnnotation.value());
                    putAttribute(attributes, attrId, baseEntity, field);
                }
                if (field.isAnnotationPresent(Boolean.class)) {
                    Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                    attrId = BigInteger.valueOf(booleanAnnotation.value());
                    String yesno = String.valueOf(booleanAnnotation.yesno());
                    putBoolean(attributes, attrId, yesno, baseEntity, field);
                }
                if (field.isAnnotationPresent(Reference.class)) {
                    Reference referenceAnnotation = field.getAnnotation(Reference.class);
                    attrId = BigInteger.valueOf(referenceAnnotation.value());
                    putReference(references, attrId, baseEntity, field);
                }
            }

            entity.setName(baseEntity.getName());
            entity.setObjectId(baseEntity.getObjectId());
            entity.setDescription(baseEntity.getDescription());
            entity.setParentId(baseEntity.getParentId());

            entity.setAttributes(attributes);
            entity.setReferences(references);
            return entity;
        }

        private void putBoolean(Map<Pair<BigInteger, Integer>, Object> attributes, BigInteger attrId, String yesno, BaseEntity baseEntity, Field field) {
            Object fieldValue = getValue(baseEntity, field);
            if (List.class.isAssignableFrom(fieldValue.getClass())) {
                List<java.lang.Boolean> booleans = (List<java.lang.Boolean>) getValue(baseEntity, field);
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

        private void putAttribute(Map<Pair<BigInteger, Integer>, Object> attributes, BigInteger attrId, BaseEntity baseEntity, Field field) {
            Object fieldValue = getValue(baseEntity, field);
            if (fieldValue == null) {
                if (field.getType() == String.class) {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_STRING);
                } else {
                    attributes.put(new Pair<>(attrId, 0), EMPTY_DATE);
                }
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                } else if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List tempAttributes = (List) fieldValue;
                    if (tempAttributes != null) {
                        for (int i = 0; i < tempAttributes.size(); i++) {
                            Object attribute = tempAttributes.get(i);
                            if (Date.class.isAssignableFrom(attribute.getClass())) {
                                attributes.put(new Pair<>(attrId, i + 1), attribute != null ? attribute.toString() : EMPTY_DATE);
                            } else {
                                attributes.put(new Pair<>(attrId, i + 1), attribute != null ? attribute.toString() : EMPTY_STRING);
                            }
                        }
                    }
                } else {
                    if (Date.class.isAssignableFrom(fieldValue.getClass())) {
                        attributes.put(new Pair<>(attrId, 0), fieldValue);
                    } else {
                        attributes.put(new Pair<>(attrId, 0), fieldValue.toString());
                    }
                }
            }
        }

        private void putReference(Map<Pair<BigInteger, Integer>, BigInteger> references, BigInteger attrId, BaseEntity baseEntity, Field field) {
            Object fieldValue = getValue(baseEntity, field);
            if (fieldValue == null) {
                references.put(new Pair<>(attrId, 0), EMPTY_INTEGER);
            } else {
                if (Set.class.isAssignableFrom(fieldValue.getClass())) {
                    Set temp = (Set) fieldValue;
                    fieldValue = Arrays.asList(temp.toArray());
                } if (List.class.isAssignableFrom(fieldValue.getClass())) {
                    List<BaseEntity> tempReferences = (List<BaseEntity>) fieldValue;
                    if (tempReferences != null) {
                        for (int i = 0; i < tempReferences.size(); i++) {
                            BaseEntity reference = tempReferences.get(i);
                            references.put(new Pair<>(attrId, i + 1), reference != null ? reference.getObjectId() : EMPTY_INTEGER);
                        }
                    }
                } else {
                    references.put(new Pair<>(attrId, 0), ((BaseEntity) fieldValue).getObjectId());
                }
            }
        }

        private Object getValue(BaseEntity baseEntity, Field field) {
            Object value;
            PropertyDescriptor property;
            try {
                property = new PropertyDescriptor(field.getName(), baseEntity.getClass());
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
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            baseEntity.setName(entity.getName());
            baseEntity.setObjectId(entity.getObjectId());
            baseEntity.setDescription(entity.getDescription());
            baseEntity.setParentId(entity.getParentId());

            Map<Pair<BigInteger, Integer>, Object> attrMap = entity.getAttributes();
            Map<Pair<BigInteger, Integer>, BigInteger> refMap = entity.getReferences();
            Field[] fields = clazz.getDeclaredFields();
            BigInteger attrId;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                    attrId = BigInteger.valueOf(attributeAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                    setValue(field, baseEntity, pairs, attrMap);
                } else if (field.isAnnotationPresent(Boolean.class)) {
                    Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                    attrId = BigInteger.valueOf(booleanAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                    String yesno = String.valueOf(booleanAnnotation.yesno());
                    setBoolean(field, baseEntity, pairs, attrMap, yesno);
                } else if (field.isAnnotationPresent(Reference.class)) {
                    Reference referenceAnnotation = field.getAnnotation(Reference.class);
                    attrId = BigInteger.valueOf(referenceAnnotation.value());
                    List<Pair<BigInteger, Integer>> pairs = getAllPairs(attrId, refMap.keySet());
                    setRef(field, baseEntity, pairs, refMap, baseEntity.getObjectId());
                }
            }
            return baseEntity;

        }

        private void setRef(Field field, BaseEntity baseEntity,
                            List<Pair<BigInteger, Integer>> pairs, Map<Pair<BigInteger, Integer>, BigInteger> refMap, BigInteger outerEntityId) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType) || Set.class.isAssignableFrom(fieldType)) {
                Object[] values = new Object[getMaxSeqNo(pairs)];
                for (Pair<BigInteger, Integer> pair : pairs) {
                    BigInteger attribute = refMap.get(pair);
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                    values[pair.getValue() - 1] = attribute != null ? getByIdRef(attribute, listClass) : null;
                }
                value = Arrays.asList(values);
                if(Set.class.isAssignableFrom(fieldType)){
                    value  = new HashSet<>(Arrays.asList(values));
                }
            } else {
                if(!pairs.isEmpty()){
                    BigInteger attribute = refMap.get(pairs.get(0));
                    value = attribute != null ? getById(attribute, fieldType) : null;
                }
            }
            setValueIntoField(field, baseEntity, value);
        }

        private void setValue(Field field, BaseEntity baseEntity,
                              List<Pair<BigInteger, Integer>> pairs, Map<Pair<BigInteger, Integer>, Object> attrMap) {
            Class fieldType = field.getType();
            Object value = null;
            if (List.class.isAssignableFrom(fieldType)) {
                Object[] values = new Object[getMaxSeqNo(pairs)];
                for (Pair<BigInteger, Integer> pair : pairs) {
                    Object attribute = attrMap.get(pair);
                    if (Date.class.isAssignableFrom(attribute.getClass())) {
                        values[pair.getValue() - 1] = attribute != null ? new Date(Timestamp.valueOf(String.valueOf(attribute)).getTime()) : null;
                    } else {
                        values[pair.getValue() - 1] = attribute;
                    }
                }
                value = Arrays.asList(values);
            } else {
                if(!pairs.isEmpty()){
                    Object attribute = attrMap.get(pairs.get(0));
                    if (Timestamp.class.isAssignableFrom(attribute.getClass())) {
                        value = attribute != null ? new Date(((Timestamp)attribute).getTime()) : null;
                    } else if(String.class.isAssignableFrom(fieldType)){
                        value = attribute;
                    } else {
                        try {
                            Method m = fieldType.getMethod("valueOf", String.class);
                            value = m.invoke(fieldType, attribute);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            setValueIntoField(field, baseEntity, value);
        }

        private void setBoolean(Field field, BaseEntity baseEntity, List<Pair<BigInteger, Integer>> pairs,
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
                if(!pairs.isEmpty()){
                    String attribute = (String) attrMap.get(pairs.get(0));
                    value = yesno.equals(attribute);
                }
            }
            setValueIntoField(field, baseEntity, value);
        }

        private void setValueIntoField(Field field, BaseEntity baseEntity, Object value) {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), baseEntity.getClass());
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
            Integer max = 0;
            for (Pair<BigInteger, Integer> pair : pairs) {
                if (pair.getValue() > max) {
                    max = pair.getValue();
                }
            }
            return max;
        }
    }
}