/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import com.civprod.writerstoolbox.NaturalLanguage.util.Parser;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import com.civprod.writerstoolbox.OpenNLP.ThreadSafeParser;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.ChainPronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.GenderPronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.NPOnlyPronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.NotPseudosentencePronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.NumberPronounPredicate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class Tester {

    public static String[][] createTokenizedDocument(String[] docRaw) {
        String[][] tokenizedDocument = new String[docRaw.length][];
        for (int i = 0; i < docRaw.length; i++) {
            tokenizedDocument[i] = docRaw[i].split(" ");
        }
        return tokenizedDocument;
    }

    public static Parse[] parseDocument(ThreadSafeParser mParser, String[] docRaw) {
        return java.util.Arrays.asList(docRaw)
                .parallelStream()
                .map((String curText) -> {
                    Parse parse;
                    try {
                        parse = mParser.parse(curText);
                    } catch (Parser.ParsingError ex) {
                        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                        parse = null;
                    }
                    return parse;
                })
                .collect(java.util.stream.Collectors.toList())
                .toArray(new Parse[docRaw.length]);
    }

    public static void main(String args[]) throws IOException {
        ThreadSafeParser mParser = new ThreadSafeParser(OpenNLPUtils.readParserModel());
        String[] docRaw = mixedPersonSet();
        String[][] tokenizedDocument = createTokenizedDocument(docRaw);
        Parse[] parses = parseDocument(mParser, docRaw);
        Dictionary femaleDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\female.dict")));
        Dictionary maleDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\male.dict")));
        Dictionary neuterDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\neuter.dict")));
        GenderPronounPredicate newGenderPronounPredicate = new GenderPronounPredicate(femaleDictionary, maleDictionary, neuterDictionary, GenderPronounPredicate.oneThird);
        Dictionary singularCardinalNumberDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\singularCardinalNumber.dict")));
        Dictionary pluralCardinalNumberDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\pluralCardinalNumber.dict")));
        Dictionary massNounDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\massNoun.dict")));
        Dictionary singularDeterminerDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\singularDeterminer.dict")));
        Dictionary pluralDeterminerDictionary = new Dictionary(new java.io.BufferedInputStream(new FileInputStream(".\\data\\datasets\\my\\pluralDeterminer.dict")));
        NumberPronounPredicate newNumberPronounPredicate = new NumberPronounPredicate(singularCardinalNumberDictionary, pluralCardinalNumberDictionary, massNounDictionary, singularDeterminerDictionary, pluralDeterminerDictionary);
        ChainPronounPredicate newChainPronounPredicate = new ChainPronounPredicate(true, new NPOnlyPronounPredicate(), newGenderPronounPredicate, newNumberPronounPredicate, new NotPseudosentencePronounPredicate());
        HobbsAlgorithm Hobbs = new HobbsAlgorithm(newChainPronounPredicate);
        String[][] labelPronouns = Hobbs.labelPronouns(tokenizedDocument, parses);
        for (int i = 0; i < tokenizedDocument.length; i++) {
            String[] curSentence = tokenizedDocument[i];
            String[] curSentenceLabels = labelPronouns[i];
            for (int j = 0; j < curSentence.length; j++) {
                System.out.print(curSentence[j]);
                String curLabel = curSentenceLabels[j];
                if ((curLabel != null) && (!curLabel.equalsIgnoreCase(PronounResolver.OTHER))) {
                    System.out.print("{");
                    System.out.print(curLabel);
                    System.out.print("}");
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    public static String[] pluralTestSet() {
        String[] sents = new String[7];
        sents[0] = "Twenty one thousand two hundred tigers ate the food .";
        sents[1] = "21200 tigers ate the food .";
        sents[2] = "I ate a dozen of candies .";
        sents[3] = "Wolf pack hunts at night .";
        sents[4] = "There are many books in the library .";
        sents[5] = "All the evidence points to guilty .";
        sents[6] = "The kids played on the playground .";
        return sents;
    }

    public static String[] singularTestSet() {
        String[] sents = new String[6];
        sents[0] = "one tiger ate the food .";
        sents[1] = "I ate a candy .";
        sents[2] = "The wolf hunted at night .";
        sents[3] = "The book I want is in the library .";
        sents[4] = "This piece evidence points to guilty .";
        sents[5] = "The kid played on the playground .";
        return sents;
    }

    public static String[] firstPersonSet() {
        String[] parses = new String[24];
        parses[0] = "Dear Princess Celestia , possibly the three most hurtful words I have ever written .";
        parses[1] = "Not for their content but for who I said them to .";
        parses[2] = "My mother Princess Celestia .";
        parses[3] = "I know what you are thinking .";
        parses[4] = "How could Princess Celestia have a daughter ?";
        parses[5] = "How can I hate such a caring mare ?";
        parses[6] = "And I do hate her , well dislike at this point , ok I am ambivalent but I have my reasons .";
        parses[7] = "Number one is how could she cover up my existence !";
        parses[8] = "I didn’t even know she was my mother until she broke down and told me when I was nine .";
        parses[9] = "Nine !";
        parses[10] = "I had already been her student three years !";
        parses[11] = "Three years !";
        parses[12] = "I guess that where it started .";
        parses[13] = "The ambition, the greed , the feeling better than anypony else , the desire to prove to my mother I was a worthy daughter .";
        parses[14] = "Maybe I better start at the beginning , I was born princess Sunset Shimmer heir apparent of the throne of the sun ( not that mom was dying any time soon ) .";
        parses[15] = "I was n’t even five minutes old when my title and name was stripped from me !";
        parses[16] = "I was taken from the palace a placed in the Canterlot orphanage .";
        parses[17] = "I spend the next nine years as Glow Light , daughter of Star Light , sister of Night Light ( Ya , Ya , laugh it up Twilight is my cousin , not that I am going to her that any time soon ) .";
        parses[18] = "At age six I passed the test for Celestia ’s school for gifted Unicorns with flying colors when I correctly identified the dragon egg as a fossil and unhatchable ( How Twilight managed hatch it I will never know but I bet they are using a rock painted to look like a dragon egg now ) .";
        parses[19] = "Three years later I found out not only was adopted but I was the daughter of Princess Celestia .";
        parses[20] = "Needless to say this caused drama with both my mothers .";
        parses[21] = "From then on I was not only Princess Celestia ’s student but her heir and a future princess .";
        parses[22] = "My feelings of entitlement only increased when I found out she was grooming me to lead the elements of harmony as the element of magic .";
        parses[23] = "Actually that bears explaining in more depth , it was just after Cadence ’s ascendance to alicorn .";
        return parses;
    }

    public static String[] mixedPersonSet() {
        String[] parses = new String[87];
        parses[0] = "The coffin was made of ebony .";
        parses[1] = "In it lay a 16 year old girl named Charity Pope .";
        parses[2] = "Charity was 5 foot 4 inches tall .";
        parses[3] = "She had long blonde hair that went down to her mid back .";
        parses[4] = "Her skin had a pale tone only seen on the dead .";
        parses[5] = "She wore a simple white dress that covered from her upper arm and chest to her ankles .";
        parses[6] = "A blonde wig covered where she had crude involuntary and illegal but successful brain surgery .";
        parses[7] = "Charity ’s father closed the lid , before the pallbearers carried the coffin to out of the chapel .";
        parses[8] = "Six months ago inside the Pope home .";
        parses[9] = "Charity lay in a double bed with her ' twin brother ' John .";
        parses[10] = "Her natural blonde hair was loosely braided .";
        parses[11] = "She wore a pair of baby blue pajamas .";
        parses[12] = "John was 5 feet 8 inches .";
        parses[13] = "He had short dirty blonde hair .";
        parses[14] = "His pajamas were true blue .";
        parses[15] = "They each wore an electronic USB programmable medical ID bracelet .";
        parses[16] = "The ' twins ' were hugging each other in their sleep .";
        parses[17] = "A female nurse walked into the bedroom .";
        parses[18] = "She wore a white blouse and brown slacks .";
        parses[19] = "She had a blue stethoscope around her neck .";
        parses[20] = "She carried a white medical case, which she set down on the desk .";
        parses[21] = "She then set about the task of separating the teenagers so she could take their vital signs .";
        parses[22] = "The Pope ' twins ' awoke staring into each other’s lawn green eyes .";
        parses[23] = "John and Charity pushed away from each other .";
        parses[24] = "Forcing Charity out of the bed and John out of the bed and on top of the nurse .";
        parses[25] = "\" Sorry . \"";
        parses[26] = "John said to the nurse before the Popes picked themselves .";
        parses[27] = "<i> ' What is going on ?";
        parses[28] = "Why was there a girl / boy in my bed ?";
        parses[29] = "HOW CAN I BE STANDING ON BOTH SIDES OF THE BED AT THE SAME TIME ! ? '";
        parses[30] = "</i> thought the Pope twins before staring at each other .";
        parses[31] = "\" You are awake , I see . \" said the nurse .";
        parses[32] = "\" Awake ?";
        parses[33] = "How long have been asleep ? \" asked teenagers at the same time .";
        parses[34] = "\" You have been catatonic and mute for the last 13 days . \"";
        parses[35] = "\" 13 days ! ?";
        parses[36] = "How did I get from playing basketball to catatonia ? \"";
        parses[37] = "\" Let us see . \"";
        parses[38] = "The nurse walked over to the desk and picked up the clipboard .";
        parses[39] = "\" John had a seizure and fell into a coma 29 days ago and was admitted to Kaiser Permanente Medical center in Fremont , CA .";
        parses[40] = "12 days later he was joined by Charity.";
        parses[41] = "3 days after that you were released into your mother 's care and she brought you here .";
        parses[42] = "You have been on home care ever since . \"";
        parses[43] = "\" Here ? \"";
        parses[44] = "\" To Atlantis . \"";
        parses[45] = "\" This is probably a dumb question but we are not underwater, right ? \"";
        parses[46] = "\" No , our town is just named after the mythical city that sank . \"";
        parses[47] = "\" Um , who is Charity ? \"";
        parses[48] = "\" That would be your female half . \"";
        parses[49] = "\" Since when has she been anything other than a mental fluke . \" said John .";
        parses[50] = "\" Hey , who are you calling a mental fluke ! ? \" said Charity .";
        parses[51] = "\" You !";
        parses[52] = " Do you know how much trouble you've caused me !";
        parses[53] = " I was uncomfortable in my own body half the time thanks to you ! \"";
        parses[54] = "\" Oh , Ya !";
        parses[55] = "You can keep that male body , I am perfectly happy in this one ! \"";
        parses[56] = "\" Says the girl who never had breasts before . \"";
        parses[57] = "\" I do not need to have them to know that I wanted them ! \"";
        parses[58] = "\" Will you stop that !";
        parses[59] = "I am a guy !";
        parses[60] = "We are not female ! \"";
        parses[61] = "\" Are Too ! \"";
        parses[62] = "John was about to retort before stopping himself .";
        parses[63] = "\" Great my male and female personas are arguing .";
        parses[64] = "What do say truce until I find out what's going on . \" said the teenagers simultaneously .";
        parses[65] = "The Pope twins turned back to the nurse .";
        parses[66] = "\" Maybe you better start at the beginning .";
        parses[67] = "Why are there two of me ?";
        parses[68] = "Why am I in Atlantis instead of Fremont ? \"";
        parses[69] = "\" The simple answer is magic is coming back and you either revered back to or transformed into a more magical form .";
        parses[70] = "We call this process magic enhancement and Human affected magically enhanced humans or MEH for short .";
        parses[71] = "Atlantis is a town formed for study of magic and the treatment of magically enhanced animals and plants .";
        parses[72] = "Your family decided to take the US Magic Administration up on their offer to relocate to Atlantis to give you fresh start .";
        parses[73] = "Any more questions ? \"";
        parses[74] = "\" Ya , why do not I remember any of this ? \"";
        parses[75] = "\" Loss of autobiographical memory is common with Chronic Atlantean Suggestible Hallucinosis . \"";
        parses[76] = "\" Atlantean what ? \"";
        parses[77] = "\" Atlantean Suggestible Hallucinosis is the cover story to hide the fact magic is coming back .";
        parses[78] = "The main ' symptoms ' are hallucinations and increased memory fluidity .";
        parses[79] = "The ' chronic ' form adds delusions , cravings and / or obsessions . \"";
        parses[80] = "\" You're covering up magic by calling it a disease ?";
        parses[81] = "How does that help if someone stubbles across magic ? \"";
        parses[82] = "\" That's where the acute form and ' memory fluidity ' comes in .";
        parses[83] = "If more than one person saw magic or even a group .";
        parses[84] = "We say that some cried werewolf and everyone else then ' remembers ' seeing a werewolf because of ' increased memory fluidity ' .";
        parses[85] = "Now Charity can you come over here so I can take your differential electrocardiogram ? \" asked the nurse .";
        parses[86] = "\" Differential electrocardiogram ? \"";
        return parses;
    }

}
