public class puzzleClass {
    private String question;
    private  String answer;
    private String hint;

    //creates the puzzle
    public puzzleClass(String question, String answer, String hint){
        this.question = question;
        this.answer = answer;
        this.hint = hint;

    }
    //Create the looping varable for a do while loop found in check solution
    public static boolean isSolved(){
        return true;

    }
    public String getQuestion(){
        return question;
    }
    //returns the hint from the list
    public String gethint(){
        return hint;
    }
    public boolean checkSolution(String input){
        return input.equalsIgnoreCase(answer);
        }

    }

