package org.mrshim.menuservice.repository;

import org.mrshim.menuservice.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends MongoRepository<Dish,String> {


}
