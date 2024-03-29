package models;

import com.fasterxml.jackson.databind.JsonNode;
import fileio.*;
import models.cards.Card;
import models.cards.heroes.Hero;
import services.CardsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Tournament {

    private ArrayList<Deck> playerOneDecks;
    private ArrayList<Deck> playerTwoDecks;
    private ArrayList<GameInput> gameInputs;


    public Tournament(final Input input) {
        playerOneDecks = parseDeckInput(input.getPlayerOneDecks());
        playerTwoDecks = parseDeckInput(input.getPlayerTwoDecks());
        gameInputs = input.getGames();
    }

    public final ArrayList<JsonNode> play() {
        ArrayList<JsonNode> results = new ArrayList<>();
        for (GameInput gameInput : gameInputs) {
            // System.out.println("Starting a new game\n\n");
            Game game = parseGame(gameInput);
            GameProcessor gameProcessor = new GameProcessor(game, gameInput.getActions());
            ArrayList<JsonNode> gameResult = gameProcessor.play();
            results.addAll(gameResult);
        }
        return results;
    }

    private Game parseGame(final GameInput gameInput) {
        Deck playerOneDeck =
                playerOneDecks.get(gameInput.getStartGame().getPlayerOneDeckIdx()).clone();
        Deck playerTwoDeck =
                playerTwoDecks.get(gameInput.getStartGame().getPlayerTwoDeckIdx()).clone();
        Hero playerOneHero =
                (Hero) CardsFactory.createCard(gameInput.getStartGame().getPlayerOneHero());
        Hero playerTwoHero = (
                Hero) CardsFactory.createCard(gameInput.getStartGame().getPlayerTwoHero());

        shuffleCards(playerOneDeck, gameInput.getStartGame().getShuffleSeed());
        shuffleCards(playerTwoDeck, gameInput.getStartGame().getShuffleSeed());

        int startingPlayer = gameInput.getStartGame().getStartingPlayer();

        return new Game(playerOneDeck, playerTwoDeck, playerOneHero, playerTwoHero, startingPlayer);
    }

    private void shuffleCards(final Deck deck, final long shuffleSeed) {
        Random random = new Random(shuffleSeed);
        Collections.shuffle(deck.getCards(), random);
    }

    private ArrayList<Deck> parseDeckInput(final DecksInput decksInput) {
        ArrayList<Deck> result = new ArrayList<>();
        for (ArrayList<CardInput> deckInput : decksInput.getDecks()) {
            ArrayList<Card> cards = new ArrayList<>();
            for (CardInput cardInput : deckInput) {
                Card card = CardsFactory.createCard(cardInput);
                cards.add(card);
            }
            Deck deck = new Deck(cards);
            result.add(deck);
        }
        return result;
    }
}
