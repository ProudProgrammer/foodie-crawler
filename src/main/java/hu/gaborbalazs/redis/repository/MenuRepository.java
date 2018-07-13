package hu.gaborbalazs.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.gaborbalazs.redis.entity.Menu;

@Repository
public interface MenuRepository extends CrudRepository<Menu, String> {
}
