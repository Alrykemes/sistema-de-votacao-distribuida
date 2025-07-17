<<<<<<<< HEAD:votacao-distribuida/src/main/java/com/votingsystem/rmi/domain/user/User.java
package com.votingsystem.rmi.domain.user;
========
package com.votingsystem.common.domain;
>>>>>>>> main:common/src/main/java/com/votingsystem/common/domain/User.java

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
<<<<<<<< HEAD:votacao-distribuida/src/main/java/com/votingsystem/rmi/domain/user/User.java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
========
public class User {
    private String id;
>>>>>>>> main:common/src/main/java/com/votingsystem/common/domain/User.java

    private String username;

    private String password;
}
