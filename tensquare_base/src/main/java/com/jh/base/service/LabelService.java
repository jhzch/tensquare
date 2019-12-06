package com.jh.base.service;

import com.jh.base.dao.LabelDao;
import com.jh.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
    private IdWorker idWorker;
    public List<Label> findAll(){
        return  labelDao.findAll();
    }

    @Cacheable(value = "lable",key = "#id")
    public Label findById(String id){
       Label label = (Label) redisTemplate.opsForValue().get("label_"+id);
       if(label==null){
           label = labelDao.findById(id).get();
           redisTemplate.opsForValue().set("label_"+id,label);
       }
        return label;
    }
    public void add(Label label){
        label.setId( idWorker.nextId()+"" );//设置ID
        labelDao.save(label);

    }


    /*** 修改标签 * @param label */
    public void update(Label label){
        labelDao.save(label);
    }

    /*** 删除标签 * @param id */
    @CacheEvict(value  ="label",key = "#label.id")
    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
       return labelDao.findAll(new Specification<Label>() {

           /*
              root:根对象，也就是要把条件封装到哪个对象中。where类型=label.getId
              query   封装的都是查询关键字，比如group by order by等
              cb   用来封装条件对象的，如果直接返回null,表示不需要任何条件
            */
           @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               List<Predicate> list = new ArrayList<>();
               if(label.getLabelname()!=null && !"".equals(label.getLabelname())){
                   Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                   list.add(predicate);
               }
               if(label.getState()!=null && !"".equals(label.getState())){
                   Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class),label.getState());
                   list.add(predicate);
               }
               Predicate[] parr = new Predicate[list.size()];
               parr = list.toArray(parr);
               return criteriaBuilder.and(parr);
            }
        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if(label.getState()!=null && !"".equals(label.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class),label.getState());
                    list.add(predicate);
                }
                Predicate[] parr = new Predicate[list.size()];
                parr = list.toArray(parr);
                return criteriaBuilder.and(parr);
            }
        },pageable);
    }
}
