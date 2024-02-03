package com.green.hoteldog.cursor;

import org.apache.ibatis.annotations.Mapper;
import org.eclipse.angus.mail.imap.protocol.Item;

import java.util.List;
import java.util.Map;

@Mapper
public interface CursorMapper {
    List<Item> getItemsByCursor(Map<String, Object> parameter);
}
