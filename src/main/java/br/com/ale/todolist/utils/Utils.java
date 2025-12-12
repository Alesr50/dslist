package br.com.ale.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target){
        
        //Copiar as propriedaes de um objeto para o outro, terceiro parametro regra para iss
        BeanUtils.copyProperties(source, target,getNullPropertyNames(source));

    };

    //pegar propriedades com valor, copy do objeto do body para o repository

    public static String[] getNullPropertyNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);
            
        //Obtendo propriedade do objeto
        //Gera um array com as propriedades
            PropertyDescriptor[] pds = src.getPropertyDescriptors();
            //Conjunto com as propriedades de valores nulos
            Set<String> emptyNames = new HashSet<>();


            //interação 
        for(PropertyDescriptor pd:pds){
            //para cada pegar o valor 
            Object srcValue  = src.getPropertyValue(pd.getName());
            if (srcValue == null){
                //se for nula, adiciona no conjunto
                emptyNames.add(pd.getName());
            }
        }
            //Array com os empty nomes, colocando o nome
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);

    }
    
}
