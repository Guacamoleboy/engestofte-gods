package alpha.domain.entity.dao;

import alpha.dao.EntityManagerDAO;
import alpha.domain.entity.entity.Entity;
import jakarta.persistence.EntityManager;

public class EntityDAO extends EntityManagerDAO<Entity> {

    public EntityDAO(EntityManager em) {
        super(em, Entity.class);
    }

}
