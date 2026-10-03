package engestofte.domain.role.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.enums.RoleName;
import jakarta.persistence.EntityManager;

public class RoleDAO extends EntityManagerDAO<Role> {

    // Attributes

    // _________________________________________________________________________________________________________________

    public RoleDAO(EntityManager em) {
        super(em, Role.class);
    }

    // _________________________________________________________________________________________________________________

    public Role getByName(RoleName name) {
        return findEntityByColumn(name, Role.Fields.NAME);
    }

}
