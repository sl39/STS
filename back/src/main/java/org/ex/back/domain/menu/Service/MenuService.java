package org.ex.back.domain.menu.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ex.back.domain.menu.Repository.MenuRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Getter
@Service
public class MenuService {
    private final MenuRepository menuRepository;
}
