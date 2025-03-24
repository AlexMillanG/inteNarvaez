package mx.edu.utez.inteNarvaez.controllers.address.dto;

import lombok.Data;
import mx.edu.utez.inteNarvaez.models.address.AddressBean;
import mx.edu.utez.inteNarvaez.models.client.ClientBean;
@Data
public class AddressDTO {

    private Long id;
    private String name;
    private String street;
    private Integer number;
    private String city;
    private String state;
    private Integer zipCode;
    private Long clientId;

    public AddressBean toEntity() {
        AddressBean addressBean = new AddressBean(name,  street,  number,  city,  state,  zipCode,clientId);
        addressBean.setId(this.id);
        addressBean.setName(this.name);
        addressBean.setStreet(this.street);
        addressBean.setNumber(this.number);
        addressBean.setCity(this.city);
        addressBean.setState(this.state);
        addressBean.setZipCode(this.zipCode);

        ClientBean clientBean = new ClientBean();
        clientBean.setId(this.clientId);
        addressBean.setClient(clientBean);
        return addressBean;
    }
}
