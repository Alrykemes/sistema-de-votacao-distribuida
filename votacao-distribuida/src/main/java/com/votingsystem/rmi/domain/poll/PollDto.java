package com.votingsystem.rmi.domain.poll;

import java.util.List;

public record PollDto(String id, String title, List<PollOption> pollOptionsList) {
}
