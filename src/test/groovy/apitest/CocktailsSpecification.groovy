package apitest

import apitest.common.BaseSpecification

import spock.lang.Unroll

/**
 * Created by demyr on 10-Nov-19.
 */
class CocktailsSpecification extends BaseSpecification {
    def setupSpec() {
        // Connect to DB and and add predefined objects
    }

    def cleanupSpec() {
        // Delete objects from DB
    }

    def "Each cocktail should has instructions how to mix"() {
        when:
        def cocktailsIds = httpHelper().get("/filter.php?c=Cocktail").getBodyAsJson().drinks.idDrink

        then:
        cocktailsIds.each {
            assert httpHelper().get("/lookup.php?i=$it").getBodyAsJson().drinks[0].strInstructions != null
        }
    }

    @Unroll
    def "Should be able to filter drinks by certain characteristics"() {
        when:
        def response = httpHelper().get("/filter.php?$characteristic=$value")

        then:
        response
                .assertStatus(200)
                .withBody { body ->
                    assert body.drinks.size() == drinksCount
                    body.drinks.each {
                        assert it.idDrink // equivalent to assert not empty or not null
                        assert it.strDrink
                        assert it.strDrinkThumb
                    }
                }
        where:
        characteristic | value             | drinksCount
        "i"            | "vodka"           | 83
        "i"            | "gin"             | 95
        "c"            | "Ordinary_Drink"  | 100
        "c"            | "Cocktail"        | 100
        "g"            | "Cocktail_glass"  | 100
        "g"            | "Champagne_flute" | 13
        "a"            | "Alcoholic"       | 100
        "a"            | "Non_Alcoholic"   | 58
    }

    def "Should be able to search cocktail by name and open it details"() {
        given:
        String cocktailName = "Blue Margarita"
        def cocktailId = httpHelper().get("/search.php?s=$cocktailName").getBodyAsJson().drinks.idDrink.toString().replaceAll("[\\[\\]]", "")

        when:
        def response = httpHelper().get("/lookup.php?i=$cocktailId")

        then:
        response
                .withBody {
                    assert it.drinks.idDrink.toString().replaceAll("[\\[\\]]", "") == cocktailId
                    assert it.drinks.strDrink.toString().replaceAll("[\\[\\]]", "") == cocktailName
                    assert it.drinks.strAlcoholic
                    assert it.drinks.strCategory
                    assert it.drinks.strGlass
                    assert it.drinks.strInstructions
                }
    }

    def "Should result empty list by filtering non existing ingredient"() {
        when:
        def response = httpHelper().get("/filter.php?i=NotExistingIngredient")

        then:
        response.assertStatus(404)
    }
}
