package org.mrshim.menuservice.repository;

import io.micrometer.observation.annotation.Observed;
import org.mrshim.menuservice.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Observed
public interface MenuRepository extends MongoRepository<Dish,String> {


}
