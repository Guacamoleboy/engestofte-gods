package engestofte.domain.role.service;

import engestofte.domain.role.dao.RoleDAO;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import engestofte.service.EntityManagerService;
import jakarta.persistence.EntityManager;

public class RoleService extends EntityManagerService<Role> {

    // Attributes
    private final RoleDAO roleDAO;

    // _________________________________________________________________________________________________________________

    public RoleService(EntityManager em) {
        super(new RoleDAO(em), Role.class);
        this.roleDAO = (RoleDAO) this.entityManagerDAO;
    }

    // _________________________________________________________________________________________________________________

    public Role getByName(RoleName name) {
        return roleDAO.getByName(name);
    }

}
