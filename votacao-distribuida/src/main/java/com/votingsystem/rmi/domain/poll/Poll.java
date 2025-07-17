package com.votingsystem.rmi.domain.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Poll {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private List<PollOption> pollOptionsList;
}
