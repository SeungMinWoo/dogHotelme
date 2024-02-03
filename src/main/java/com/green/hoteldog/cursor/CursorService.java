package com.green.hoteldog.cursor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.imap.protocol.Item;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CursorService {
    private final CursorMapper cursorMapper;

    public List<Item> getItemsByCursor(Long cursorId, int pageSize) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cursorId", cursorId);
        parameters.put("pageSize", pageSize);
        return cursorMapper.getItemsByCursor(parameters);
    }
}
