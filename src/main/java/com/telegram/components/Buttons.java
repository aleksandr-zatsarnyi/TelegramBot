package com.telegram.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Помощь");
    private static final InlineKeyboardButton JOKE_BUTTON = new InlineKeyboardButton("Анекдот");
    private static final InlineKeyboardButton DIVINATION_BUTTON = new InlineKeyboardButton("Делать?");

    public static InlineKeyboardMarkup inlineMarkup() {
        HELP_BUTTON.setCallbackData("/help");
        JOKE_BUTTON.setCallbackData("/joke");
        DIVINATION_BUTTON.setCallbackData("/divination");

        List<InlineKeyboardButton> rowInline = List.of(JOKE_BUTTON, DIVINATION_BUTTON, HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
