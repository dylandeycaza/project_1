import java.util.Scanner;

/**
 * So what the floridianToothRecords program does is manages dental records for a Florida family,
 * storing tooth types by Incisors, Bicuspids, and Missing. in a 3D array, and providing
 * options to print records, extract teeth, and calculate quadratic root canal indices.
 *
 * @author Dylan M. De Ycaza
 */
public class floridianToothRecords {

    /**
     * Main method to run the Floridian Tooth Records program.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // 1. This ensures we get number of people in family max 6 and more than 1
        int familySize = -1;

        System.out.print("Please enter number of people in the family : ");
        while (familySize < 1 || familySize > 6) {
            if (keyboard.hasNextInt()) {
                familySize = keyboard.nextInt();
                if (familySize < 1 || familySize > 6) {
                    System.out.print("Invalid number of people, try again         : ");
                }
            } else {
                keyboard.next();
                System.out.print("Invalid number of people, try again         : ");
            }
        }
        keyboard.nextLine(); // goes to new line

        // Data structures
        String[] names = new String[familySize];
        // 3d array
        char[][][] teeth = new char[familySize][2][8];
        // Store number of teeth per person
        int[][] toothLengths = new int[familySize][2];

        // 2. Populate family member names and tooth records
        for (int i = 0; i < familySize; i++) {
            System.out.print("Please enter the name for family member " + (i + 1) + "   : ");
            names[i] = keyboard.nextLine().trim();

            // Get uppers
            teeth[i][0] = getValidToothRow(keyboard, "uppers", names[i], toothLengths[i], 0);

            // Get lowers
            teeth[i][1] = getValidToothRow(keyboard, "lowers", names[i], toothLengths[i], 1);
        }

        // 3. Menu loop
        System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
        boolean running = true;
        while (running) {
            String menuInput = keyboard.nextLine().trim();
            if (menuInput.isEmpty()) {
                System.out.print("Invalid menu option, try again              : ");
                continue;
            }
            char menuOption = Character.toUpperCase(menuInput.charAt(0));

            switch (menuOption) {
                case 'P':
                    printRecords(familySize, names, teeth, toothLengths);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                case 'E':
                    extractTooth(keyboard, familySize, names, teeth, toothLengths);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                case 'R':
                    calculateRoots(familySize, teeth, toothLengths);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                case 'X':
                    System.out.println("Exiting the Floridian Tooth Records :-)");
                    running = false;
                    break;
                default:
                    System.out.print("Invalid menu option, try again              : ");
                    break;
            }
        }
        keyboard.close();
    } // end of main method

    /**
     * This method asks for and validates a row of teeth (uppers or lowers).
     *
     * @param keyboard       Scanner for user input
     * @param layerName     String description of the layer ("uppers" or "lowers")
     * @param memberName    Name of the family member
     * @param lengthTracker Array to store the length of uppers and lowers
     * @param rowIndex      0 for uppers, 1 for lowers
     * @return Validated array of characters representing teeth
     */
    private static char[] getValidToothRow(Scanner keyboard, String layerName, String memberName, int[] lengthTracker, int rowIndex) {
        char[] rowTeeth = new char[8];
        System.out.print("Please enter the " + layerName + " for " + memberName + "       : ");
        while (true) {
            String input = keyboard.nextLine().trim();

            if (input.length() > 8) {
                System.out.print("Too many teeth, try again                   : ");
                continue;
            }

            boolean isValid = true;
            char[] tempTeeth = new char[input.length()];
            for (int i = 0; i < input.length(); i++) {
                char c = Character.toUpperCase(input.charAt(i));
                if (c == 'I' || c == 'B' || c == 'M') {
                    tempTeeth[i] = c;
                } else {
                    isValid = false;
                    break;
                }
            }

            if (!isValid) {
                System.out.print("Invalid teeth types, try again              : ");
            } else {
                lengthTracker[rowIndex] = input.length();
                for (int i = 0; i < input.length(); i++) {
                    rowTeeth[i] = tempTeeth[i];
                }
                return rowTeeth;
            }
        }
    }// end of getValidToothRow method

    /**
     * This method prints the family's complete teeth records with 1-based indexing.
     *
     * @param familySize Number of people in the family
     * @param names      Array of family member names
     * @param teeth      3D array of tooth characters
     * @param lengths    2D array monitoring lengths of each row
     */
    private static void printRecords(int familySize, String[] names, char[][][] teeth, int[][] lengths) {
        System.out.println();
        for (int i = 0; i < familySize; i++) {
            System.out.println(names[i]);

            // Print Uppers
            System.out.print("  Uppers:  ");
            for (int j = 0; j < lengths[i][0]; j++) {
                System.out.print((j + 1) + ":" + teeth[i][0][j] + (j < lengths[i][0] - 1 ? "  " : ""));
            }
            System.out.println();

            // Print Lowers
            System.out.print("  Lowers:  ");
            for (int j = 0; j < lengths[i][1]; j++) {
                System.out.print((j + 1) + ":" + teeth[i][1][j] + (j < lengths[i][1] - 1 ? "  " : ""));
            }
            System.out.println();
        }
    }// end of printRecords method

    /**
     * This method handles the tooth extracting process for family member, layer, and tooth position.
     *
     * @param keyboard   Scanner for user input
     * @param familySize Number of people in the family
     * @param names      Array of family member names
     * @param teeth      3D array of tooth characters
     * @param lengths    2D array tracking monitoring of each row
     */
    private static void extractTooth(Scanner keyboard, int familySize, String[] names, char[][][] teeth, int[][] lengths) {
        int memberIndex = -1;
        System.out.print("Which family member                         : ");
        while (memberIndex == -1) {
            String nameInput = keyboard.nextLine().trim();
            for (int i = 0; i < familySize; i++) {
                if (names[i].equalsIgnoreCase(nameInput)) {
                    memberIndex = i;
                    break;
                }
            }
            if (memberIndex == -1) {
                System.out.print("Invalid family member, try again            : ");
            }
        }

        int rowIndex = -1;
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        while (rowIndex == -1) {
            String layerInput = keyboard.nextLine().trim();
            if (!layerInput.isEmpty()) {
                char layer = Character.toUpperCase(layerInput.charAt(0));
                if (layer == 'U') {
                    rowIndex = 0;
                } else if (layer == 'L') {
                    rowIndex = 1;
                }
            }
            if (rowIndex == -1) {
                System.out.print("Invalid layer, try again                    : ");
            }
        }

        int toothNum = -1;
        int maxTeeth = lengths[memberIndex][rowIndex];
        System.out.print("Which tooth number                          : ");
        while (toothNum == -1) {
            if (keyboard.hasNextInt()) {
                int inputNum = keyboard.nextInt();
                keyboard.nextLine(); // consume newline
                if (inputNum < 1 || inputNum > maxTeeth) {
                    System.out.print("Invalid tooth number, try again             : ");
                } else if (teeth[memberIndex][rowIndex][inputNum - 1] == 'M') {
                    System.out.print("Missing tooth, try again                    : ");
                } else {
                    toothNum = inputNum;
                }
            } else {
                keyboard.next(); // consume invalid token
                if (keyboard.hasNextLine()) {
                    keyboard.nextLine(); // consume rest of line
                }
                System.out.print("Invalid tooth number, try again             : ");
            }
        }


        teeth[memberIndex][rowIndex][toothNum - 1] = 'M';
    }// end of extractTooth method

    /**
     *  This method calculates and displays the root canal indices (roots of the quadratic equation Ix^2 + Bx - M).
     *
     * @param familySize Number of people in the family
     * @param teeth      3D array of tooth characters
     * @param lengths    2D array tracking lengths of each row
     */
    private static void calculateRoots(int familySize, char[][][] teeth, int[][] lengths) {
        int totalI = 0;
        int totalB = 0;
        int totalM = 0;

        for (int i = 0; i < familySize; i++) {
            for (int row = 0; row < 2; row++) {
                for (int j = 0; j < lengths[i][row]; j++) {
                    char t = teeth[i][row][j];
                    if (t == 'I') totalI++;
                    else if (t == 'B') totalB++;
                    else if (t == 'M') totalM++;
                }
            }
        }

        // Equation: I*x^2 + B*x - M = 0
        double a = totalI;
        double b = totalB;
        double c = -totalM;

        if (a == 0) {
            System.out.println("Coefficient I is 0, cannot compute quadratic roots.");
            return;
        }

        double discriminant = (b * b) - (4 * a * c);
        if (discriminant < 0) {
            System.out.println("No real roots found (negative discriminant).");
            return;
        }

        double root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
        double root2 = (-b - Math.sqrt(discriminant)) / (2 * a);

        System.out.printf("One root canal at     %.2f\n", root1);
        System.out.printf("Another root canal at %.2f\n", root2);
    }// end of calculate Roots method
}// end of the public class
