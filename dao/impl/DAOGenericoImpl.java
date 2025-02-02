package com.lucascorreia.dao.impl;
import com.lucascorreia.dao.DAOGenerico;
import com.lucascorreia.util.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DAOGenericoImpl<V> implements DAOGenerico<V>  {
    protected Map<Integer, V> map = new LinkedHashMap<>(16);
    private int contador = 0;

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }
    public Map<Integer, V> getMap() {
        return map;
    }
    public void setMap(Map<Integer, V> map) {
        this.map = map;
    }

    private Field recuperarCampoIdentificador(V obj) {
        Field[] campos = obj.getClass().getDeclaredFields();
        for (Field campo : campos) {
            if (campo.isAnnotationPresent(Id.class)) {
                return campo;
            }
        }
        throw new RuntimeException(
                "A classe " + obj.getClass().getName() + " deve possuir " +
                        "um campo anotado com @Id.");
    }
    public V incluir(V obj) {
        Field campo = recuperarCampoIdentificador(obj);
        try {
            campo.setAccessible(true);
            campo.set(obj, ++contador);
            return map.put(contador, obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public V alterar(V obj) {
        Field campo = recuperarCampoIdentificador(obj);

        try {
            campo.setAccessible(true);
            campo.set(obj, ++contador);
            return map.put(contador, obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public V remover(Integer id) {
        return map.remove(id);
    }

    public V recuperarPorId(Integer id) {
        return map.get(id);
    }

    public List<V> recuperarTodos() {
        return new ArrayList<>(map.values());
    }

}
