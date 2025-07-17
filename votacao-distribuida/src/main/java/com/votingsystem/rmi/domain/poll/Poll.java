package com.votingsystem.rmi.domain.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Poll {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private List<PollOption> pollOptionsList;
}
