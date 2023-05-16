package com.telegram.service;

import com.telegram.components.Buttons;
import com.telegram.config.BotConfig;
import com.telegram.dto.GifDto;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URL;

import static com.telegram.components.BotCommands.LIST_OF_COMMANDS;

@Slf4j
@Component
public class CounterTelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;

    private final JokeProvider provider;
    private final YesNoProvider yesNoProvider;

    public CounterTelegramBot(BotConfig config, JokeProvider provider, YesNoProvider yesNoProvider) {
        super(config.getToken());
        this.config = config;
        this.provider = provider;
        this.yesNoProvider = yesNoProvider;

        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        log.info("Update Received");
        long chatId;
        String userName;
        String receivedMessage;

        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        String[] message = receivedMessage.split("@");
        switch (message[0]) {
            case "/start" -> startBot(chatId, userName);
            case "/help" -> sendHelpText(chatId);
            case "/joke" -> sendJoke(chatId);
            case "/divination" -> test(chatId);
            default -> {
            }
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! I'm a Telegram bot.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendHelpText(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(com.telegram.components.BotCommands.HELP_TEXT);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void sendJoke(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(provider.getJoke().getContent());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void test(long chatId) {
        SendAnimation animation = new SendAnimation();
        animation.setChatId(chatId);
        GifDto dto = yesNoProvider.getGif();
        InputFile file = new InputFile();
        try {
            file.setMedia(String.valueOf(new URL(dto.getImage())));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        animation.setAnimation(file);

        try {
            execute(animation);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
