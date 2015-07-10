package com.bj58.oceanus.client.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;
import com.mdeng.oceanusex.dal.AutoIncrementId;
import com.mdeng.oceanusex.dal.DBField;
import com.mdeng.oceanusex.dal.OceanusResult;
import com.mdeng.oceanusex.dal.OceaunsEntity;
import com.mdeng.oceanusex.dal.Pagination;
import com.mdeng.oceanusex.exceptions.OceanusDuplicateException;
import com.mdeng.oceanusex.exceptions.OceanusNotFoundException;
import com.mdeng.oceanusex.exceptions.OceanusSqlException;
import com.mdeng.oceanusex.orm.BaseDaoEx;

/**
 * Base database access service
 * 
 * @author hui.deng
 *
 * @param <T>
 */
public class SupportService<T extends OceaunsEntity> extends BaseDaoEx {

  protected Class<T> clazz;

  public SupportService(Class<T> clazz, String configPath) {
    super(configPath);
    this.clazz = clazz;
  }

  /**
   * Get by primary key id, exception if not found
   * 
   * @param id
   * @return
   * @throws Exception
   */
  public T getById(Object id) throws Exception {
    return single(String.format("%s=?", getAutoIncrementFieldName()), id);
  }

  /**
   * Get by primary key id
   * 
   * @param id
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  public List<T> getById(Object[] id) throws Exception {
    if (id == null || id.length == 0) return Lists.newArrayList();
    StringBuilder where = new StringBuilder(getAutoIncrementFieldName() + " in (");
    for (Object object : id) {
      where.append("?,");
    }
    where.deleteCharAt(where.length() - 1).append(")");
    Pagination pgn = new Pagination(1, id.length);
    return pagination(where.toString(), pgn, id).getOceanusList();
  }

  /**
   * Get by page
   * 
   * @param pgn
   * @return
   * @throws Exception
   */
  public OceanusResult<T> getByPage(Pagination pgn) throws Exception {
    return pagination("1 = 1", pgn);
  }

  /**
   * Get by field, return list value
   * 
   * @param field
   * @param value
   * @return
   * @throws Exception
   */
  public OceanusResult<T> getByField(DBField field, Pagination pgn) throws Exception {
    return pagination(String.format("%s=?", field.getName()), pgn, field.getValue());
  }

  /**
   * Get by fields, return single value, null if not found
   * 
   * @param fields
   * @param values
   * @return
   * @throws Exception
   */
  public T getByFields(DBField... fields) throws Exception {
    String where = "1=1 ";
    for (DBField field : fields) {
      where += "and " + field.getName() + "=? ";
    }

    return singleIfAny(where, DBField.values(Lists.newArrayList(fields)));
  }

  /**
   * Count by field
   * 
   * @param field
   * @return
   * @throws Exception
   */
  public int count(DBField field) throws Exception {
    return count(String.format("%s=?", field.getName()), field.getValue());
  }

  /**
   * Count by fields
   * 
   * @param fields
   * @param values
   * @return
   * @throws Exception
   */
  public int count(List<DBField> fields) throws Exception {
    String where = "1=1 ";
    for (DBField field : fields) {
      where += "and " + field.getName() + "=? ";
    }

    return count(where, DBField.values(fields));
  }

  /**
   * Get by fields, return list value
   * 
   * @param fields
   * @param values
   * @param pgn
   * @return
   * @throws Exception
   */
  public OceanusResult<T> getByFields(List<DBField> fields, Pagination pgn) throws Exception {
    String where = "1=1 ";
    for (DBField field : fields) {
      where += "and " + field.getName() + "=? ";
    }

    return pagination(where, pgn, DBField.values(fields));
  }

  /**
   * Insert into database
   * 
   * @param t
   * @return
   * @throws Exception
   */
  public void insert(T t) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).insert().build();

    Object[] params = getFieldsValues(t);
    insertAndGetKey(sql, params);
  }

  /**
   * Batch insert in one transaction
   * 
   * @param lst
   * @throws Exception
   */
  public void insert(List<T> lst) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).insert().build();

    List<Object[]> params = Lists.newArrayList();
    for (T t : lst) {
      Object[] p = getFieldsValues(t);
      params.add(p);
    }

    batch(sql, params);
  }

  /**
   * Update an object
   * 
   * @param t
   * @return
   * @throws Exception
   */
  public void update(T t) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).update(t).build();

    List<Object> params = Lists.newArrayList(getFieldsValues(t));
    params.add(getAutoIncrementFieldValue(t));
    excuteUpdate(sql, params.toArray());
  }

  /**
   * Update an object on fields
   * 
   * @param t
   * @param fields
   * @throws Exception
   */
  public void update(T t, String... fields) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).update(t, fields).build();

    List<Object> params = Lists.newArrayList(getFieldsValues(t, Lists.newArrayList(fields)));
    params.add(getAutoIncrementFieldValue(t));
    excuteUpdate(sql, params.toArray());
  }

  /**
   * Delete by key
   * 
   * @param key
   * @throws Exception
   */
  public void delete(Object key) throws Exception {
    delete(String.format("%s=?", getAutoIncrementFieldName()), key);
  }

  /**
   * Delete by condition
   * 
   * @param where
   * @param values
   * @throws Exception
   */
  public void delete(String where, Object... values) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).delete().where(where).build();
    excuteUpdate(sql, values);
  }

  /**
   * Custom pagination
   * 
   * @param where
   * @param pgn
   * @param values
   * @return
   * @throws Exception
   */
  public OceanusResult<T> pagination(String where, Pagination pgn, Object... values)
      throws Exception {
    pgn = checkPgn(pgn);

    // list
    String sql = OceanusSqlBuilder.instance(clazz).select().where(where).pagination(pgn).build();
    int start = (pgn.getPageNo() - 1) * pgn.getPageSize();
    List<Object> params = Lists.newArrayList(values);
    params.add(start);
    params.add(pgn.getPageSize());

    List<T> lst = excuteQuery(clazz, sql, params.toArray());

    // count
    int count = count(where, values);

    // set result to pagination
    pgn.setTotal(count);

    return new OceanusResult<T>(count, lst);
  }

  // /////////////////////////////////////////////////////////////////////////

  protected T single(String where, Object... values) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).select().where(where).build();
    List<T> lst = excuteQuery(clazz, sql, values);
    if (lst.size() == 0) {
      throw new OceanusNotFoundException("Expected single but 0 found");
    } else if (lst.size() >= 2) {
      throw new OceanusDuplicateException("Expected single but " + lst.size() + " found");
    }

    return lst.get(0);
  }

  protected T singleIfAny(String where, Object... values) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).select().where(where).build();
    List<T> lst = excuteQuery(clazz, sql, values);
    if (lst.size() > 0) {
      return lst.get(0);
    }

    return null;
  }

  protected int count(String where, Object... values) throws Exception {
    String sql = OceanusSqlBuilder.instance(clazz).select("count(1)").where(where).build();
    return excuteCount(sql, values);
  }

  protected Pagination checkPgn(Pagination pgn) {
    return pgn != null ? pgn : new Pagination();
  }

  protected <E> boolean exists(List<E> lst) {
    return lst.size() > 0;
  }

  protected Object[] getFieldsValues(T t) throws IllegalAccessException, InvocationTargetException {
    return getFieldsValues(t, null);
  }

  protected Object[] getFieldsValues(T t, List<String> fieldNames) throws IllegalAccessException,
      InvocationTargetException {
    List<Object> params = Lists.newArrayList();
    for (Field field : MappingAnnotationUtil.getAllFields(clazz)) {
      String name = MappingAnnotationUtil.getDBCloumnName(clazz, field);
      if (name != null
          && !(name.equalsIgnoreCase("id") || field.isAnnotationPresent(AutoIncrementId.class))) {
        if (fieldNames != null && !fieldNames.contains(name)) continue;
        // 约定不更新或插入Id
        Method method = MappingAnnotationUtil.getGetterMethod(clazz, field);
        params.add(method.invoke(t));
      }
    }
    return params.toArray();
  }

  private String getAutoIncrementFieldName() throws Exception {
    for (Field field : MappingAnnotationUtil.getAllFields(clazz)) {
      if (field.isAnnotationPresent(AutoIncrementId.class)) {
        return field.getName();
      }
    }

    for (Field field : MappingAnnotationUtil.getAllFields(clazz)) {
      if (field.getName().equals("id")) {
        return "id";
      }
    }

    throw new OceanusSqlException("can not find identity column");
  }

  private Object getAutoIncrementFieldValue(T t) throws Exception {
    for (Field field : MappingAnnotationUtil.getAllFields(clazz)) {
      if (field.isAnnotationPresent(AutoIncrementId.class)) {
        Method method = MappingAnnotationUtil.getGetterMethod(clazz, field);
        return method.invoke(t);
      }
    }

    for (Field field : MappingAnnotationUtil.getAllFields(clazz)) {
      if (field.getName().equals("id")) {
        Method method = MappingAnnotationUtil.getGetterMethod(clazz, field);
        return method.invoke(t);
      }
    }

    throw new OceanusSqlException("can not find identity column");
  }
}
