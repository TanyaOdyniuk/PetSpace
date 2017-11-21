package com.netcracker.dao.converter;

import com.netcracker.dao.Entity;
import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.Boolean;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.dao.managerapi.ManagerAPI;
import com.netcracker.model.BaseEntity;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Component
public class Converter {
    @Autowired
    private ManagerAPI manager;

    private final Date EMPTY_DATE = new Date(-1);
    private final String EMPTY_STRING = "-1";

    public Entity convertToEntity(BaseEntity baseEntity) {
        Class baseEntityClass = baseEntity.getClass();
        Entity entity = new Entity();
        Map<Pair<Integer, Integer>, Object> attributes = new HashMap<>();
        Map<Pair<Integer, Integer>, Integer> references = new HashMap<>();

        if (baseEntityClass.isAnnotationPresent(ObjectType.class)) {
            ObjectType objectType = (ObjectType) baseEntityClass.getAnnotation(ObjectType.class);
            Integer objectTypeId = objectType.value();
            entity.setObjectTypeId(objectTypeId);
        }
        Field[] fields = baseEntityClass.getDeclaredFields();
        Integer attrId;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class)) {
                Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                attrId = attributeAnnotation.value();
                putAttribute(attributes, attrId, baseEntity, field);
            }
            if (field.isAnnotationPresent(Boolean.class)) {
                Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                attrId = booleanAnnotation.value();
                String yesno = String.valueOf(booleanAnnotation.yesno());
                putBoolean(attributes, attrId, yesno, baseEntity, field);
            }
            if (field.isAnnotationPresent(Reference.class)) {
                Reference referenceAnnotation = field.getAnnotation(Reference.class);
                attrId = referenceAnnotation.value();
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

    private void putBoolean(Map<Pair<Integer, Integer>, Object> attributes, Integer attrId, String yesno, BaseEntity baseEntity, Field field) {
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

    private void putAttribute(Map<Pair<Integer, Integer>, Object> attributes, Integer attrId, BaseEntity baseEntity, Field field) {
        Object fieldValue = getValue(baseEntity, field);
        if (List.class.isAssignableFrom(fieldValue.getClass())) {
            List tempAttributes = (List) getValue(baseEntity, field);
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
                attributes.put(new Pair<>(attrId, 0), fieldValue != null ? fieldValue.toString() : EMPTY_DATE);
            } else {
                attributes.put(new Pair<>(attrId, 0), fieldValue != null ? fieldValue.toString() : EMPTY_STRING);
            }
        }
    }


    private void putReference(Map<Pair<Integer, Integer>, Integer> references, Integer attrId, BaseEntity baseEntity, Field field) {
        Object fieldValue = getValue(baseEntity, field);
        if (List.class.isAssignableFrom(fieldValue.getClass())) {
            List<BaseEntity> tempReferences = (List<BaseEntity>) fieldValue;
            if (tempReferences != null) {
                for (int i = 0; i < tempReferences.size(); i++) {
                    BaseEntity reference = tempReferences.get(i);
                    if (reference != null) {
                        references.put(new Pair<>(attrId, i + 1), reference.getObjectId());
                    }
                }
            }
        } else {
            if (fieldValue != null) {
                if (!(BaseEntity.class.isAssignableFrom(fieldValue.getClass()))) {
                    references.put(new Pair<>(attrId, 0), ((BaseEntity) fieldValue).getObjectId());
                }
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

    public <T extends BaseEntity> T convertToBaseEntity(Entity entity, Class clazz) {
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

        Map<Pair<Integer, Integer>, Object> attrMap = entity.getAttributes();
        Map<Pair<Integer, Integer>, Integer> refMap = entity.getReferences();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Attribute.class) || field.isAnnotationPresent(Boolean.class)) {
                Attribute attributeAnnotation = field.getAnnotation(Attribute.class);
                Integer attrId = attributeAnnotation.value();
                List<Pair<Integer, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                setValue(field, baseEntity, pairs, attrMap);
            }
            if (field.isAnnotationPresent(Boolean.class)) {
                Boolean booleanAnnotation = field.getAnnotation(Boolean.class);
                Integer attrId = booleanAnnotation.value();
                List<Pair<Integer, Integer>> pairs = getAllPairs(attrId, attrMap.keySet());
                String yesno = String.valueOf(booleanAnnotation.yesno());
                setBoolean(field, baseEntity, pairs, attrMap, yesno);
            }
            if (field.isAnnotationPresent(Reference.class)) {
                Reference referenceAnnotation = field.getAnnotation(Reference.class);
                Integer attrId = referenceAnnotation.value();
                List<Pair<Integer, Integer>> pairs = getAllPairs(attrId, refMap.keySet());
                setRef(field, baseEntity, pairs, refMap);
            }
        }
        return baseEntity;

    }

    private void setRef(Field field, BaseEntity baseEntity,
                        List<Pair<Integer, Integer>> pairs, Map<Pair<Integer, Integer>, Integer> refMap) {
        Class fieldType = field.getType();
        Object value = null;
        if (List.class.isAssignableFrom(fieldType)) {
            Object[] values = new Object[getMaxSeqNo(pairs)];
            for (Pair<Integer, Integer> pair : pairs) {
                Integer attribute = refMap.get(pair);
                if (BaseEntity.class.isAssignableFrom(attribute.getClass())) {
                    values[pair.getValue()] = attribute != null ? manager.getById(attribute, fieldType) : null;
                }
            }
            value = values;
        } else {
            Integer attribute = refMap.get(pairs.get(0));
            if (BaseEntity.class.isAssignableFrom(attribute.getClass())) {
                value = attribute != null ? manager.getById(attribute, fieldType) : null;
            }
        }
        setValueIntoField(field, baseEntity, value);
    }

    private void setValue(Field field, BaseEntity baseEntity,
                          List<Pair<Integer, Integer>> pairs, Map<Pair<Integer, Integer>, Object> attrMap) {
        Class fieldType = field.getType();
        Object value;
        if (List.class.isAssignableFrom(fieldType)) {
            Object[] values = new Object[getMaxSeqNo(pairs)];
            for (Pair<Integer, Integer> pair : pairs) {
                Object attribute = attrMap.get(pair);
                if (Date.class.isAssignableFrom(attribute.getClass())) {
                    values[pair.getValue()] = attribute != null ? new Date(Timestamp.valueOf(String.valueOf(attribute)).getTime()) : null;
                } else {
                    values[pair.getValue()] = attribute != null ? String.valueOf(attribute) : null;
                }
            }
            value = values;
        } else {
            Object attribute = attrMap.get(pairs.get(0));
            if (Date.class.isAssignableFrom(attribute.getClass())) {
                value = attribute != null ? new Date(Timestamp.valueOf(String.valueOf(attribute)).getTime()) : null;
            } else {
                value = attribute != null ? String.valueOf(attribute) : null;
            }
        }
        setValueIntoField(field, baseEntity, value);
    }

    private void setBoolean(Field field, BaseEntity baseEntity, List<Pair<Integer, Integer>> pairs,
                            Map<Pair<Integer, Integer>, Object> attrMap, String yesno) {
        Class fieldType = field.getType();
        Object value;
        if (List.class.isAssignableFrom(fieldType)) {
            java.lang.Boolean[] values = new java.lang.Boolean[getMaxSeqNo(pairs)];
            for (Pair<Integer, Integer> pair : pairs) {
                String attribute = (String) attrMap.get(pair);
                values[pair.getValue()] = yesno.equals(attribute);
            }
            value = values;
        } else {
            String attribute = (String) attrMap.get(pairs.get(0));
            value = yesno.equals(attribute);
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

    private List<Pair<Integer, Integer>> getAllPairs(Integer attrId, Set<Pair<Integer, Integer>> keyPairs) {
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for (Pair<Integer, Integer> pair : pairs) {
            if (pair.getKey().equals(attrId)) {
                pairs.add(pair);
            }
        }
        return pairs;
    }

    private Integer getMaxSeqNo(List<Pair<Integer, Integer>> pairs) {
        Integer max = -1;
        for (Pair<Integer, Integer> pair : pairs) {
            if (pair.getValue() > max) {
                max = pair.getValue();
            }
        }
        return max;
    }
}
