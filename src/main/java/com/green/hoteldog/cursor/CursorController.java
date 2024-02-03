package com.green.hoteldog.cursor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.imap.protocol.Item;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CursorController {
    private final CursorService cursorService;

    @GetMapping("/items")
    public List<Item> getItemsByCursor(@RequestParam(value = "cursorId", required = false) Long cursorId,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return cursorService.getItemsByCursor(cursorId, pageSize);
    }
    //커서 기반 페이지네이션. front 에서 cursorId (hotel Pk 같은  primary key 또는 유니크 한 값,)
    // pageSize ( 한 페이지에 보내줄 개수) 를 보내줌.

    /*
    <select id="getItemsByCursor" parameterType="map" resultType="com.example.model.Item">
        SELECT *
        FROM items
        <if test="cursorId != null">
            WHERE id &lt; #{cursorId}
        </if>
        ORDER BY created_date DESC
        LIMIT #{pageSize}
    </select>
     */
}
