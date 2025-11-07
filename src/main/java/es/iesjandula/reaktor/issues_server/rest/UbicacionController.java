package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dto.UbicacionDTO;
import es.iesjandula.reaktor.issues_server.entity.UbicacionEntity;
import es.iesjandula.reaktor.issues_server.repository.IUbicacionRepository;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/issues/ubicaciones")
public class UbicacionController
{
    @Autowired
    private IUbicacionRepository ubicacionRepository;

    /** Listar todas las ubicaciones (para el desplegable, PROFESOR puede verlas) */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping
    public ResponseEntity<?> listarUbicaciones()
    {
        try
        {
            List<UbicacionDTO> resultado = this.ubicacionRepository
                .findAllByOrderByNombreAsc()
                .stream()
                .map(u -> new UbicacionDTO(u.getId(), u.getNombre()))
                .collect(Collectors.toList());

            return ResponseEntity.ok(resultado);
        }
        catch (Exception e)
        {
            String msg = "Error inesperado en listarUbicaciones(): " + e.getMessage();
            log.error(msg, e);
            IssuesServerError err = new IssuesServerError(0, msg, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMapError());
        }
    }

    /** Crear nueva ubicación (solo administración) */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping
    public ResponseEntity<?> crearUbicacion(@RequestBody UbicacionDTO dto)
    {
        try
        {
            if (dto.getNombre() == null || dto.getNombre().isBlank())
            {
                String errorString = "El nombre de la ubicación es obligatorio.";
                log.error(errorString);
                throw new IssuesServerError(10, errorString);
            }

            String nombreNormalizado = dto.getNombre().trim();

            if (this.ubicacionRepository.findByNombreIgnoreCase(nombreNormalizado).isPresent())
            {
                String errorString = "Ya existe una ubicación con ese nombre.";
                log.error(errorString);
                throw new IssuesServerError(11, errorString);
            }

            UbicacionEntity entity = new UbicacionEntity();
            entity.setNombre(nombreNormalizado);
            this.ubicacionRepository.saveAndFlush(entity);

            dto.setId(entity.getId());
            log.info("Ubicacion creada: {}", entity);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }
        catch (IssuesServerError ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMapError());
        }
        catch (Exception e)
        {
            String msg = "Error inesperado creando la ubicación: " + e.getMessage();
            log.error(msg, e);
            IssuesServerError err = new IssuesServerError(0, msg, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMapError());
        }
    }
}
