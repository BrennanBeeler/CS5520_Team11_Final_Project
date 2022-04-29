package edu.neu.madcourse.modernmath.problem_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Difficulty;
import edu.neu.madcourse.modernmath.assignments.Operator;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.problemselection.ProblemSelectionActivity;


public class ProblemScreenActivity extends AppCompatActivity {

    private String userAnswer = "";
    private Difficulty difficulty;
    private int numOfQuestions;
    private long time = 0;
    private int operand1;
    private int operand2;
    private int answer;
    String classId = "";
    private String assignmentCode = "";
    private User user;
    private Operator[] operators;
    private int correctAnswers = 0;
    private int incorrectAnswer = 0;
    private int previousCorrect;
    private int overallAnswers;
    EditText answerField;
    TextView numberOfQuestionsField;
    DatabaseReference db;
    Task<DataSnapshot> assignmentRef;
    Task<DataSnapshot> userRef;
    private Instant start;
    private long time_spent;
    Operator selectedOp;

    private TextView test_text;
    private SpeechRecognizer speechRecognizer;
    TextToSpeech textToSpeech;
    private Intent speechIntent;
    private boolean is_listening = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_screen);
        Bundle extras = getIntent().getExtras();
        db = FirebaseDatabase.getInstance().getReference();
        initSpeechService();
        initTTS();
        // Set up action bar
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("Practice Math Facts");
            actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        if (extras != null) {
            difficulty = (Difficulty) extras.getSerializable("play_level");
            numOfQuestions = extras.getInt("numberOfQuestions");
            time = extras.getLong("time");
            assignmentCode = extras.getString("assignmentCode");
            user = (User) extras.get("active_user");
            operators = ((List<Operator>) extras.getSerializable("play_mode")).toArray(new Operator[0]);
            classId = extras.getString("classKey");
        }
        if (operators.length == 0 || user == null) {
            Toast.makeText(getBaseContext(), getString(R.string.error),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        initUser();
        if (assignmentCode != null) {
            initAssignment();
        } else if (time > 0){
            showStartDialog(getString(R.string.start_challenge)).show();
        } else {
            init();
        }
    }

    private void initTTS() {
        ImageView tts = findViewById(R.id.tts_btn);
        textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS)
                    textToSpeech.setLanguage(Locale.US);
                else {
                    //To-DO: show error
                }
            }
        });

        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(String.valueOf(operand1), TextToSpeech.QUEUE_ADD, null, "");
                String operatorString;
                switch (selectedOp) {
                    case ADDITION: operatorString = "plus";
                    break;
                    case SUBTRACTION: operatorString = "minus";
                    break;
                    case MULTIPLICATION: operatorString = "multiplied by";
                    break;
                    default:operatorString = "divided by";
                }
                textToSpeech.speak(operatorString, TextToSpeech.QUEUE_ADD, null, "");
                textToSpeech.speak(String.valueOf(operand2), TextToSpeech.QUEUE_ADD, null, "");
            }
        });
    }

    private void initSpeechService() {
        this.test_text = findViewById(R.id.answerText);
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        this.speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        this.speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                test_text.setText("...");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                answerField.setText("");
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> words = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                String user_input = words.get(0);

                boolean isNumeric = false;

                try
                {
                    user_input = user_input.replaceAll("\\s+","");

                    if (Integer.parseInt(user_input) >= 0)
                    {
                        isNumeric = true;
                    }
                    else
                    {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getBaseContext(), "Not numeric", Toast.LENGTH_SHORT).show();
                }

                if (isNumeric)
                {
                    setAnswerField(user_input);
                }
                else
                {
                    setAnswerField("");
                }
                answerField.setText(getUserAnswer());

                is_listening = false;
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private boolean checkPermission()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void getPermissions()
    {
        int REQUEST_CODE = 9882;
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
    }

    public void testOnClick(View view)
    {
        if (checkPermission())
        {
            if(this.is_listening)
            {
                this.speechRecognizer.stopListening();
                this.is_listening = false;
            }
            else
            {
                this.speechRecognizer.startListening(this.speechIntent);
                this.is_listening = true;
            }
        }
        else
        {
            getPermissions();
        }
    }

    @Override
    public void onDestroy()
    {
        speechRecognizer.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        menu.findItem(R.id.name).setTitle(user.firstName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(ProblemScreenActivity.this, ProblemSelectionActivity.class);
                intent.putExtra("active_user", this.user);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(numOfQuestions > 0 || time > 0) {
            postScores();
        }
        previousCorrect = correctAnswers;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initUser();
        if (assignmentCode != null) {
            initAssignment();
        }
    }


    private void initUser() {
        userRef = db.child("users").child(user.email).get();
        userRef.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("answers").getValue() != null) {
                    overallAnswers = Math.toIntExact((Long) dataSnapshot.child("answers").getValue());
                }
            }
        });
    }

    private void initAssignment() {
        assignmentRef = db.child("classes").child(classId).child("assignments")
                .child(assignmentCode).child("student_assignments").child(user.email).get();
        assignmentRef.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    //TO-DO : show invalid user error
                } else if (dataSnapshot.child("time_spent").getValue() != null) {
                    try {
                        Integer correct = Math.toIntExact((Long) dataSnapshot.child("num_correct").getValue());
                        Integer incorrect = Math.toIntExact((Long) dataSnapshot.child("num_incorrect").getValue());
                        numOfQuestions -= correct + incorrect;
                        correctAnswers = correct;
                        previousCorrect = correct;
                        incorrectAnswer = incorrect;
                        time_spent = (long) dataSnapshot.child("time_spent").getValue();
                        if (time > 0) {
                            time -= time_spent;
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if ( numOfQuestions <= 0 && time <= 0) {
                        //TO-DO: invalid assignment state
                        finish();
                    }else {
                        Dialog dialog = showStartDialog(getString(R.string.continue_challenge));
                        dialog.show();
                    }
                }
            }
        });
    }

    private void init() {
        answerField = findViewById(R.id.answerText);
        answerField.setShowSoftInputOnFocus(false);
        answerField.setCursorVisible(false);

        if (numOfQuestions > 0) {
            numberOfQuestionsField = findViewById(R.id.numberofQuestions);
            String text = String.valueOf(numOfQuestions);
            numberOfQuestionsField.setText(text);
            findViewById(R.id.number_icon).setVisibility(View.VISIBLE);
        }
        TextView timer = findViewById(R.id.timer);

        generateQuestion();
        if (time > 0) {
            findViewById(R.id.timer_icon).setVisibility(View.VISIBLE);
            new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long l) {
                    String timeString = String.valueOf(l / 1000);
                    timer.setText(timeString);
                    time -= 1000;
                }

                @Override
                public void onFinish() {
                    postScores();
                    showEndDialog("").show();
                }
            }.start();
        }
    }

    private Dialog showStartDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String message = s + '\n';
        if (numOfQuestions > 0) {
            message = message + '\n' + String.format(getString(R.string.number_of_Questions),
                    numOfQuestions);
        }
        if (time > 0) {
            message = message + '\n' + String.format(getString(R.string.time),
                    String.valueOf(time / 1000));
        }

        builder.setMessage(message)
                .setPositiveButton(R.string.start_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        init();
                        start = Instant.now();
                    }
                });
        builder.setCancelable(false);
        return builder.create();
    }

    private Dialog showEndDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String text = "";
        if (!s.isEmpty()) {
            text = s;
        }
        text = text + String.format(getString(R.string.correct_answers), correctAnswers) + '\n' +
                String.format(getString(R.string.incorrect_answers), incorrectAnswer);

        builder.setMessage(text)
                .setPositiveButton(R.string.home, (dialogInterface, i) -> {
                    finish();
                });
        builder.setCancelable(false);
        return builder.create();
    }

    public void numberPressed(View view) {
        TextView textView = (TextView) view;
        String number = textView.getText().toString();
        setAnswerField(number);
    }

    private void setAnswerField(String number) {
        if(!number.isEmpty() && !(Integer.parseInt(number) > 9999)) {
            if (getUserAnswer().isEmpty()) {
                setUserAnswer(number);
                answerField.setText(getUserAnswer());
            } else if (getUserAnswer().length() >= 4) {
                //To-DO: show error
            } else {
                setUserAnswer(getUserAnswer() + number);
                answerField.setText(getUserAnswer());
            }
        } else {
            //TO-DO: show error
        }
    }

    public void clear(View view) {
        if (!getUserAnswer().isEmpty()) {
            setUserAnswer(getUserAnswer().substring(0, getUserAnswer().length() - 1));
            answerField.setText(getUserAnswer());
        }
    }

    public void submit(View view) {
        if (!getUserAnswer().isEmpty()) {
            if (getUserAnswer().equals(String.valueOf(getAnswer()))) {
                correctAnswers++;
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen), "Correct Answer", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.primary));
                snackbar.show();
            } else {
                incorrectAnswer++;
                Snackbar snackbar = Snackbar.make(findViewById(R.id.problem_screen), "Incorrect!! Answer is: " + getAnswer(), Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.teacher_incomplete));
                snackbar.show();
            }
            --numOfQuestions;
            if (numOfQuestions > 0) {
                String text = String.valueOf(numOfQuestions);
                numberOfQuestionsField.setText(text);
            } else if (numOfQuestions == 0) {
                postScores();
                showEndDialog("").show();
            }
            setUserAnswer("");
            answerField.setText("");
            generateQuestion();
        }
    }

    private void postScores() {
        if (classId != null || assignmentCode != null) {
            Instant stop = Instant.now();
            Map<String, Object> assignmentMap = new HashMap<>();
            assignmentMap.put("num_correct", correctAnswers);
            assignmentMap.put("num_incorrect", incorrectAnswer);
            assignmentMap.put("time_spent", Duration.between(start, stop).toMillis() + time_spent);
            db.child("classes").child(classId).child("assignments")
                    .child(assignmentCode).child("student_assignments").child(user.email)
                    .updateChildren(assignmentMap);
            start = null;
            time = 0;
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("answers", overallAnswers + correctAnswers - previousCorrect);
        db.child("users").child(user.email).updateChildren(userMap);
    }

    private void generateQuestion() {

        int selectedOpIndex = generateRandom(operators.length, 0);
        selectedOp = operators[selectedOpIndex];

        switch (selectedOp) {
            case ADDITION:
                generateAdd();
                break;
            case SUBTRACTION:
                generateSub();
                break;
            case MULTIPLICATION:
                generateMulti();
                break;
            default:
                generateDiv();
        }
        TextView op1 = findViewById(R.id.operand_1);
        TextView op2 = findViewById(R.id.operand_2);
        op1.setText(String.valueOf(getOperand1()));
        op2.setText(String.valueOf(getOperand2()));

        TextView op = findViewById(R.id.operator);
        op.setText(String.valueOf(selectedOp.getValue()));
        setAnswer(selectedOp);
    }

    private void generateAdd() {
        switch (difficulty) {
            case EASY:
                setOperand1(generateRandom(10, 0));
                setOperand2(generateRandom(10, 0));
                break;
            case MEDIUM:
                setOperand1(generateRandom(90, 10));
                setOperand2(generateRandom(90, 10));
                break;
            default:
                setOperand1(generateRandom(900, 100));
                setOperand2(generateRandom(900, 100));
        }
    }

    private void generateSub() {
        switch (difficulty) {
            case EASY:
                setOperand1(generateRandom(10, 0));
                setOperand2(generateRandom(10, 0));
                break;
            case MEDIUM:
                setOperand1(generateRandom(90, 10));
                setOperand2(generateRandom(90, 10));
                break;
            default:
                setOperand1(generateRandom(900, 100));
                setOperand2(generateRandom(900, 100));
        }
        if (getOperand1() < getOperand2()) {
            generateSub();
        }
    }

    private void generateMulti() {
        switch (difficulty) {
            case EASY:
                setOperand1(generateRandom(10, 1));
                break;
            case MEDIUM:
                setOperand1(generateRandom(20, 10));
                break;
            default:
                setOperand1(generateRandom(30, 20));
        }
        setOperand2(generateRandom(10, 1));
    }

    private void generateDiv() {
        switch (difficulty) {
            case EASY:
                setOperand1(generateRandom(100, 0));
                break;
            case MEDIUM:
                setOperand1(generateRandom(500, 100));
                break;
            default:
                setOperand1(generateRandom(1000, 500));
        }
        setOperand2(generateRandom(9, 2));
        if (getOperand1() % getOperand2() != 0 || getOperand1() < getOperand2()) {
            generateDiv();
        }
    }

    private void setAnswer(Operator operator) {
        switch (operator) {
            case ADDITION:
                setAnswer(getOperand1() + getOperand2());
                break;
            case SUBTRACTION:
                setAnswer(getOperand1() - getOperand2());
                break;
            case MULTIPLICATION:
                setAnswer(getOperand1() * getOperand2());
                break;
            default:
                setAnswer(getOperand1() / getOperand2());
        }
    }

    private int generateRandom(int bound, int offset) {
        Random random = new Random();
        return random.nextInt(bound) + offset;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}