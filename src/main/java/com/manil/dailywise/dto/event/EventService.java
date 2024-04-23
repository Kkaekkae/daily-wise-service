package com.manil.dailywise.dto.event;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
public class EventService {
    private ArrayList<EventDTO> events = new ArrayList<>();

    public ArrayList<EventDTO> getEvents() {
        return events;
    }
}
