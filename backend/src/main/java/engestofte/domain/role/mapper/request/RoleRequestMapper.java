package engestofte.domain.role.mapper.request;

import engestofte.domain.role.dto.request.RoleRequestDTO;
import engestofte.domain.role.entity.Role;

public class RoleRequestMapper {

    // _________________________________________________________________________________________________________________

    // Maps RoleRequestDTO to Role entity
    // __________________________________
    //
    //       RoleRequestDTO
    //               ↓
    //       RoleRequestMapper
    //               ↓
    //       Role
    //
    // ____________________
    // Tested: NO
    // Last Tested: N/A

    // _________________________________________________________________________________________________________________

    public static Role toEntity(RoleRequestDTO dto) {

        return Role.builder()
                .name(dto.getName())
                .build();
    }

}
