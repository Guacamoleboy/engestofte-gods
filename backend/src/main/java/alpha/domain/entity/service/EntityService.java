package alpha.domain.entity.service;

import alpha.domain.entity.dao.EntityDAO;
import alpha.domain.entity.entity.Entity;
import alpha.service.EntityManagerService;

public class EntityService extends EntityManagerService<Entity> {

    public EntityService(EntityDAO entityDAO) {
        super(entityDAO, Entity.class);
    }

}
