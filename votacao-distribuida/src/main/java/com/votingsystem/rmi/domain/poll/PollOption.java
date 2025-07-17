package com.votingsystem.rmi.domain.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PollOption {
    private static final long serialVersionUID = 1L;

    private String id;
    private String text;
    private int votes;
}
