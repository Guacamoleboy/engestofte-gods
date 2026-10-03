package engestofte.domain.role.mapper.response;

import engestofte.domain.role.dto.response.RoleResponseDTO;
import engestofte.domain.role.entity.Role;

public class RoleResponseMapper {

    // _________________________________________________________________________________________________________________

    // Maps Role entity to RoleResponseDTO
    // ___________________________________
    //
    //       Role
    //               ↓
    //       RoleResponseMapper
    //               ↓
    //       RoleResponseDTO
    //
    // ____________________
    // Tested: NO
    // Last Tested: N/A

    // _________________________________________________________________________________________________________________

    public static RoleResponseDTO toDTO(Role role) {

        RoleResponseDTO dto = new RoleResponseDTO();

        dto.setId(role.getId());
        dto.setName(role.getName());

        return dto;
    }

}
