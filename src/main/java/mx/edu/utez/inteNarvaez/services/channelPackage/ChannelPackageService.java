package mx.edu.utez.inteNarvaez.services.channelPackage;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.inteNarvaez.config.ApiResponse;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channel.ChannelRepository;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageRepository;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageStatus;
import mx.edu.utez.inteNarvaez.models.contract.ContractRepository;
import mx.edu.utez.inteNarvaez.services.email.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = SQLException.class)
@RequiredArgsConstructor
public class ChannelPackageService {

    private final ChannelPackageRepository channelPackageRepository;
    private final ChannelRepository channelRepository;
    private final EmailService emailService;
    private final ContractRepository contractRepository;

    public ResponseEntity<ApiResponse> findAll() {
        try {
            return new ResponseEntity<>(new ApiResponse(channelPackageRepository.findAll(), HttpStatus.OK, null, false), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los paquetes de canales", true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> save(ChannelPackageBean channelPackageBean) {
        try {
            if (channelPackageBean.getName() == null || channelPackageBean.getName().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un nombre válido", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getDescription() == null || channelPackageBean.getDescription().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa una descripción válida", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getAmount() == null || channelPackageBean.getAmount() < 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un monto válido", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getChannels() == null || channelPackageBean.getChannels().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el paquete debe contener al menos un canal", true), HttpStatus.BAD_REQUEST);
            }

            for (ChannelBean channelBean : channelPackageBean.getChannels()) {
                Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());
                if (foundChannel.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error: el canal con ID " + channelBean.getId() + " no existe", true), HttpStatus.NOT_FOUND);
                }
            }

            channelPackageBean.setStatus(ChannelPackageStatus.DISPONIBLE);
            ChannelPackageBean savedPackage = channelPackageRepository.save(channelPackageBean);
            return new ResponseEntity<>(new ApiResponse(savedPackage, HttpStatus.CREATED, "Paquete guardado correctamente", false), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<ApiResponse> Update(ChannelPackageBean channelPackageBean) {
        try {

            if (channelPackageBean.getId() == null){
                return new ResponseEntity<>(new ApiResponse(channelPackageBean,HttpStatus.BAD_REQUEST,"error, el id es requerido,",true),HttpStatus.BAD_REQUEST);
            }

            Optional<ChannelPackageBean> foundPackage = channelPackageRepository.findById(channelPackageBean.getId());

            if (foundPackage.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"error, el paquete que intentas actualizar no existe"),HttpStatus.NOT_FOUND);
            }

            if (channelPackageBean.getName() == null || channelPackageBean.getName().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un nombre válido", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getDescription() == null || channelPackageBean.getDescription().isBlank()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa una descripción válida", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getAmount() == null || channelPackageBean.getAmount() < 0) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: ingresa un monto válido", true), HttpStatus.BAD_REQUEST);
            }

            if (channelPackageBean.getChannels() == null || channelPackageBean.getChannels().isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Error: el paquete debe contener al menos un canal", true), HttpStatus.BAD_REQUEST);
            }

            for (ChannelBean channelBean : channelPackageBean.getChannels()) {
                Optional<ChannelBean> foundChannel = channelRepository.findById(channelBean.getId());
                if (foundChannel.isEmpty()) {
                    return new ResponseEntity<>(new ApiResponse(null, HttpStatus.NOT_FOUND, "Error: el canal con ID " + channelBean.getId() + " no existe", true), HttpStatus.NOT_FOUND);
                }
            }

            channelPackageBean.setUuid(foundPackage.get().getUuid());
            channelPackageBean.setStatus(ChannelPackageStatus.DISPONIBLE);
            ChannelPackageBean savedPackage = channelPackageRepository.save(channelPackageBean);

           List<String> stringList = contractRepository.findDistinctEmailsByChannelPackage(channelPackageBean.getId());

           emailService.UpdatePackageEmail(stringList,savedPackage);

            return new ResponseEntity<>(new ApiResponse(savedPackage, HttpStatus.CREATED, "Paquete actaulizado correctamente", false), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el paquete de canales: " + e.getMessage(), true), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ApiResponse> findByUuid(UUID uuid){

        Optional<ChannelPackageBean> foundPackage = channelPackageRepository.findChannelPackageBeanByUuid(uuid);

        if (foundPackage.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(null,HttpStatus.NOT_FOUND,"error, el paquete que buscas no existe"),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponse(foundPackage.get(),HttpStatus.OK,null,false),HttpStatus.OK);
    }

}
