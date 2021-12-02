package com.baoying.enginex.executor.grouping;

import com.sun.mail.imap.protocol.ID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

@Data
//@AllArgsConstructor
public class Role {
    private Integer id ;
    private String name;
    private Auth auth;
    private List<Auth> authList;

    public Role(Integer id, String name, Auth auth, List<Auth> authList) {
        this.id = id;
        this.name = name;
        this.auth = auth;
        this.authList = authList;
    }

    public Role(Integer id, String name, Auth auth) {
        this.id = id;
        this.name = name;
        this.auth = auth;
    }
}
