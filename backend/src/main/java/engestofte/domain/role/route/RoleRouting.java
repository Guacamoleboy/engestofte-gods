// Pathing
// _______
// src/main/java/alpha/domain/role/route/RoleRouting.java

package engestofte.domain.role.route;

import engestofte.crud.CRUDRouting;
import engestofte.domain.role.controller.RoleController;
import engestofte.domain.role.entity.Role;
import engestofte.domain.role.service.RoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class RoleRouting extends CRUDRouting<Role> {

    // Attributes

    // _________________________________________________________________________________________________________________

    public RoleRouting(EntityManagerFactory emf) {
        super("/role", createController(emf));
    }

    // _________________________________________________________________________________________________________________

    private static RoleController createController(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        RoleService roleService = new RoleService(em);
        return new RoleController(roleService);
    }

}
