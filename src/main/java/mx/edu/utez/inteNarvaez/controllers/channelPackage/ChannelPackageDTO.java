package mx.edu.utez.inteNarvaez.controllers.channelPackage;

import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ChannelPackageDTO {

    private Long id;
    private String name;
    private String description;
    private Double amount;
    private Set<ChannelBean> channels = new HashSet<>();


    public ChannelPackageBean toEntity(){
        // Convert the Set to a List
        List<ChannelBean> channelList = new ArrayList<>(channels);
        return new ChannelPackageBean(name, description, amount, channelList);
    }

    public ChannelPackageBean toEntityUpdate(){
        return new ChannelPackageBean(id,name,description,amount,channels);
    }
}
