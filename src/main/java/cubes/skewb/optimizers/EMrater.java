package cubes.skewb.optimizers;

import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is a java version of the algrater provided at https://skewbschool.com/alg-rater.html
 * This rater is created by Elias Malomgré, so all credits for this algrater go to him.
 *
 */
public class EMrater implements AlgRater {
    private static final double TIER1 = 1.25;
    private static final double TIER2 = 1.0;
    private static final double TIER3 = 0.75;
    private static final double REDUCTION1 = 0.25;
    private static final double REDUCTION2 = 0.5;
    private static final double REDUCTION3 = 0.75;
    private static final double REDUCTION4 = 1.0;
    private static final double BONUS1 = 0.25;
    private static final double BONUS2 = 0.5;
    private static final double BONUS3 = 0.75;
    private static final double BONUS4 = 1.0;

    private static final Pattern[][] TRIGGERS = {
            // TIER1
            {
                    Pattern.compile("(r' R r )|(R' B R )|(B' b B )|(b' r b )"),
                    Pattern.compile("(r' R' r )|(R' B' R )|(B' b' B )|(b' r' b )"),
                    Pattern.compile("(R r' R' )|(B R' B' )|(b B' b' )|(r b' r' )"),
                    Pattern.compile("(R r R' )|(B R B' )|(b B b' )|(r b r' )"),
                    Pattern.compile("(r B r' )|(R b R' )|(B r B' )|(b R b' )"),
                    Pattern.compile("(r' B' r )|(R' b' R )|(B' r' B )|(b' R' b )")
            },
            // TIER2
            {
                    Pattern.compile("(r R r )|(R B R )|(B b B )|(b r b )"),
                    Pattern.compile("(r R' r )|(R B' R )|(B b' B )|(b r' b )"),
                    Pattern.compile("(R' r R' )|(B' R B' )|(b' B b' )|(r' b r' )"),
                    Pattern.compile("(R r' R )|(B R' B )|(b B' b )|(r b' r )"),
                    Pattern.compile("(r' R r' )|(R' B R' )|(B' b B' )|(b' r b' )"),
                    Pattern.compile("(R' r R )|(B' R B )|(b' B b )|(r' b r )"),
                    Pattern.compile("(R' r' R )|(B' R' B )|(b' B' b )|(r' b' r )")
            },
            // TIER3
            {
                    Pattern.compile("(r R r' )|(R B R' )|(B b B' )|(b r b' )"),
                    Pattern.compile("(r R' r' )|(R B' R' )|(B b' B' )|(b r' b' )"),
                    Pattern.compile("(R r R )|(B R B )|(b B b )|(r b r )"),
                    Pattern.compile("(r' R' r' )|(R' B' R' )|(B' b' B' )|(b' r' b' )"),
                    Pattern.compile("(R' r' R' )|(B' R' B' )|(b' B' b' )|(r' b' r' )"),
                    Pattern.compile("(r B' r' )|(R b' R' )|(B r' B' )|(b R' b' )"),
                    Pattern.compile("(r' B r )|(R' b R )|(B' r B )|(b' R b )")
            },
            // DEDUCTION1
            {
                    Pattern.compile("(R' r' R' r' )|(B' R' B' R' )|(b' B' b' B' )|(r' b' r' b' )"),
                    Pattern.compile("(R r R' r )|(B R B' R )|(b B b' B )|(r b r' b )"),
                    Pattern.compile("(r B r )|(R b R )|(B r B )|(b R b )"),
                    Pattern.compile("(r' B' r' )|(R' b' R' )|(B' r' B' )|(b' R' b' )"),
                    Pattern.compile("(B R' B R' )|(R r' R r' )|(b B' b B' )|(r b' r b' )")
            },
            // DEDUCTION2
            {
                    Pattern.compile("(R r R r )|(B R B R )|(b B b B )|(r b r b )")
            },
            // DEDUCTION3
            {
                    // Empty array
            },
            // DEDUCTION4
            {
                    // Empty array
            },
            // BONUS1
            {
                    // Empty array
            },
            // BONUS2
            {
                    Pattern.compile("(r' R r R )|(R' B R B )|(B' b B b )|(b' r b r )"),
                    Pattern.compile("(B R r R' )|(b B R B' )|(r b B b' )|(R r b r' )"),
                    Pattern.compile("(B' R r R' )|(b' B R B' )|(r' b B b' )|(R' r b r' )")
            }
    };

    private static final Map<String, String[]> rotations = new HashMap<>();

    static {
        rotations.put("z", new String[]{"b","b'","r","r'","B","B'","R","R'","z","z'","z2","L","L'","F","F'","l","l'","f","f'"});
        rotations.put("zi", new String[]{"R","R'","B","B'","r","r'","b","b'","z","z'","z2","f","f'","l","l'","F","F'","L","L'"});
        rotations.put("z2", new String[]{"B","B'","b","b'","R","R'","r","r'","z","z'","z2","F","F'","f","f'","L","L'","l","l'"});
        rotations.put("x", new String[]{"f","f'","r","r'","l","l'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("xi", new String[]{"r","r'","R","R'","b","b'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("x2", new String[]{"r","r'","R","R'","b","b'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("y", new String[]{"r","r'","R","R'","b","b'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("yi", new String[]{"r","r'","R","R'","b","b'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("y2", new String[]{"r","r'","R","R'","b","b'","B","B'","l","l'","L","L'","f","f'","F","F'"});
        rotations.put("inverse", new String[]{"r'","r","R'","R","b'","b","B'","B","z'","z","z2","l'","l","L'","L","f'","f","F'","F"});
        rotations.put("space", new String[]{"r ","r' ","R ","R' ","b ","b' ","B ","B' ","z ","z' ","z2 ","l ","l' ","L ","L' ","f ","f' ","F ","F' "});
    }


    public List<String> rate(Set<String> input) {
        List<String> ratedAlgs = new ArrayList<>();

        List<AlgRating> algRatings = new ArrayList<>();
        for (String alg : input) {
            if (alg.matches("([rRbB][']?[ ]?){2,}")) {
                AlgRating ratedAlg = rateAlg(alg, "");
                if (ratedAlg != null && ratedAlg.getScore() > 0) {
                    algRatings.add(ratedAlg);
                }
            }
        }

        // Sort the algRatings based on score in descending order
        algRatings.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        // Convert AlgRating objects to the desired string format
        for (AlgRating algRating : algRatings) {
            ratedAlgs.add((algRating.getScore() + " " + algRating.getOptimizedAlg()).strip());
        }

        return ratedAlgs;
    }


    private static AlgRating rateAlg(String input, String cancelMove) {
        double score = 0;
        input += " ";
        String temp = input;

        if (!cancelMove.equals("")) {
            while (temp.charAt(0) == '\'' || temp.charAt(0) == ' ') {
                temp = temp.substring(1);
            }
            if (temp.charAt(0) != cancelMove.charAt(0)) {
                score = -100000;
            }
            temp = temp.substring(1);
            while (temp.charAt(0) == '\'' || temp.charAt(0) == ' ') {
                temp = temp.substring(1);
            }
        }

        for (int i = 0; i < TRIGGERS.length; i++) {
            for (Pattern regex : TRIGGERS[i]) {
                int matchCount = countMatches(regex, input);
                if (i == 0) {
                    score += matchCount * TIER1;
                } else if (i == 1) {
                    score += matchCount * TIER2;
                } else if (i == 2) {
                    score += matchCount * TIER3;
                } else if (i >= 3 && i <= 6) {
                    score -= matchCount * new double[]{REDUCTION1, REDUCTION2, REDUCTION3, REDUCTION4}[i - 3];
                } else if (i >= 7) {
                    score += matchCount * new double[]{BONUS1, BONUS2, BONUS3, BONUS4}[i - 7];
                }
            }
        }

        score += moveCountReward(input);

        return new AlgRating(score, rotateOptimally(input));
    }

    private static int countMatches(Pattern pattern, String input) {
        java.util.regex.Matcher matcher = pattern.matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int moveCount(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'r' || c == 'R' || c == 'b' || c == 'B') {
                count++;
            }
        }
        return count;
    }

    private static double moveCountReward(String input) {
        int move = moveCount(input);
        if (move < 9) {
            return (9 - move) * 1.75;
        } else if (move > 9) {
            return (9 - move) * 1.25;
        }
        return 0;
    }

    public static String rotate(String input, String rotation) {
        input = input.replace("’", "'"); // Replacing curly apostrophes with straight ones

        switch (rotation) {
            case "z":
                return applyRotation(input, rotations.get("z"));
            case "z'":
                return applyRotation(input, rotations.get("zi"));
            case "z2":
                return applyRotation(input, rotations.get("z2"));
            case "x":
                return applyRotation(input, rotations.get("x"));
            case "x'":
                return applyRotation(input, rotations.get("xi"));
            case "x2":
                return applyRotation(input, rotations.get("x2"));
            case "y":
                return applyRotation(input, rotations.get("y"));
            case "y'":
                return applyRotation(input, rotations.get("yi"));
            case "y2":
                return applyRotation(input, rotations.get("y2"));
            case "space":
                return applyRotation(input, rotations.get("space"));
            case "inverse":
                // Split the input, reverse it, join it back, and then apply the inverse rotation
                String[] splitInput = input.split(" ");
                List<String> reversedInput = Arrays.asList(splitInput);
                Collections.reverse(reversedInput);
                return applyRotation(String.join(" ", reversedInput), rotations.get("inverse"));
            case "zrot":
                return removeZRotations(input);
            default:
                return input;
        }
    }

    private static String removeZRotations(String input) {
        if (input.isEmpty()) {
            return "";
        }

        String[] inputArray = input.split(" ");
        List<String> output = new ArrayList<>();

        for (int i = 0; i < inputArray.length; i++) {
            String move = inputArray[i];

            String[] remainingMoves;
            String rotatedString;

            switch (move) {
                case "z":
                    // Apply zi rotation to the rest of the array
                    remainingMoves = Arrays.copyOfRange(inputArray, i + 1, inputArray.length);
                    rotatedString = applyRotation(String.join(" ", remainingMoves), rotations.get("zi"));
                    inputArray = rotatedString.split(" ");
                    i = -1; // Reset the index to process the rotated string
                    break;
                case "z'":
                    // Apply z rotation to the rest of the array
                    remainingMoves = Arrays.copyOfRange(inputArray, i + 1, inputArray.length);
                    rotatedString = applyRotation(String.join(" ", remainingMoves), rotations.get("z"));
                    inputArray = rotatedString.split(" ");
                    i = -1; // Reset the index
                    break;
                case "z2":
                    // Apply z2 rotation to the rest of the array
                    remainingMoves = Arrays.copyOfRange(inputArray, i + 1, inputArray.length);
                    rotatedString = applyRotation(String.join(" ", remainingMoves), rotations.get("z2"));
                    inputArray = rotatedString.split(" ");
                    i = -1; // Reset the index
                    break;
                default:
                    output.add(move);
            }
        }

        return String.join(" ", output).trim();
    }

    private static String applyRotation(String input, String[] rotation) {
        String[] inputArray = input.split(" ");
        StringBuilder output = new StringBuilder();

        for (String move : inputArray) {
            switch (move) {
                case "r":
                    output.append(rotation[0]).append(" ");
                    break;
                case "r'":
                    output.append(rotation[1]).append(" ");
                    break;
                case "R":
                    output.append(rotation[2]).append(" ");
                    break;
                case "R'":
                    output.append(rotation[3]).append(" ");
                    break;
                case "b":
                    output.append(rotation[4]).append(" ");
                    break;
                case "b'":
                    output.append(rotation[5]).append(" ");
                    break;
                case "B":
                    output.append(rotation[6]).append(" ");
                    break;
                case "B'":
                    output.append(rotation[7]).append(" ");
                    break;
                case "z":
                    output.append(rotation[8]).append(" ");
                    break;
                case "z'":
                    output.append(rotation[9]).append(" ");
                    break;
                case "z2":
                    output.append(rotation[10]).append(" ");
                    break;
                // ... continue for other moves
                default:
                    output.append(move).append(" "); // Keep the move as it is if not found in the rotation
            }
        }

        return output.toString().trim();
    }

    private static String rotateOptimally(String input) {
        if (input.matches(".*r.*") && input.matches(".*R.*") && input.matches(".*B.*") && input.matches(".*b.*")) {
            return calculateOptimal4genRotation(input);
        } else if (input.matches(".*r.*") && input.matches(".*R.*") && input.matches(".*B.*")) {
            return input;
        } else if (input.matches(".*r.*") && input.matches(".*R.*") && input.matches(".*b.*")) {
            return rotate(input, "z'");
        } else if (input.matches(".*b.*") && input.matches(".*R.*") && input.matches(".*B.*")) {
            return rotate(input, "z");
        } else if (input.matches(".*r.*") && input.matches(".*b.*") && input.matches(".*B.*")) {
            return rotate(input, "z2");
        } else if (input.matches(".*r.*") && input.matches(".*b'.*")) {
            return rotate(input, "z'");
        } else if (input.matches(".*B.*") && input.matches(".*b'.*")) {
            return rotate(input, "z2");
        } else if (input.matches(".*R.*") && input.matches(".*B.*")) {
            return rotate(input, "z");
        } else if (input.matches(".*r.*") && input.matches(".*B.*")) {
            return calculateOptimal2genRotation(input);
        } else if (input.matches(".*R.*") && input.matches(".*b.*")) {
            return calculateOptimal2genRotation(rotate(input, "z"));
        }
        return input;
    }

    private static String calculateOptimal2genRotation(String input) {
        String bestRotation = "";
        int leastBmoves = Integer.MAX_VALUE;

        String[] rotationsToCheck = new String[]{"", "z2"};
        for (String rotation : rotationsToCheck) {
            String rotatedInput = rotate(input, rotation);
            int bCount = countOccurrences(rotatedInput, "B");

            if (bCount < leastBmoves) {
                leastBmoves = bCount;
                bestRotation = rotatedInput;
            }
        }

        return bestRotation;
    }

    private static int countOccurrences(String input, String move) {
        return input.length() - input.replace(move, "").length();
    }

    private static String calculateOptimal4genRotation(String input) {
        String bestRotation = "";
        int leastBmoves = Integer.MAX_VALUE;

        String[] rotationsToCheck = new String[]{"", "z", "z'", "z2"};
        for (String rotation : rotationsToCheck) {
            String rotatedInput = rotate(input, rotation);
            int bCount = countOccurrences(rotatedInput, "b");

            if (bCount < leastBmoves) {
                leastBmoves = bCount;
                bestRotation = rotatedInput;
            }
        }

        return bestRotation;
    }
}
