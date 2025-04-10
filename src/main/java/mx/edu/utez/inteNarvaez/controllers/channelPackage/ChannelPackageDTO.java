package mx.edu.utez.inteNarvaez.controllers.channelPackage;

import lombok.Data;
import mx.edu.utez.inteNarvaez.models.channel.ChannelBean;
import mx.edu.utez.inteNarvaez.models.channelPackage.ChannelPackageBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class ChannelPackageDTO {

    private Long id;
    private String name;
    private String description;
    private Set<ChannelBean> channels = new HashSet<>();

/*
    public ChannelPackageBean toEntity(){
        return new ChannelPackageBean(name, description, amount, (List<ChannelBean>) channels);
    }*/
    public ChannelPackageBean toEntity(){
        return new ChannelPackageBean(name, description, new ArrayList<>(channels));
    }

    public ChannelPackageBean toEntityUpdate(){
        return new ChannelPackageBean(id,name,description,new ArrayList<>(channels));
    }
}
