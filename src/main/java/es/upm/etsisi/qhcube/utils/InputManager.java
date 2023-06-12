package es.upm.etsisi.qhcube.utils;

import es.upm.etsisi.qhcube.dto.InputDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
@AllArgsConstructor
public class InputManager {
    public void randomExchange(InputDTO inputDTO) {
        int j = (int) (Math.random() * 4);
        int k = (int) (Math.random() * 4);

        if (j != k) {
            inputDTO.exchange(j, k);
        }
    }
}
