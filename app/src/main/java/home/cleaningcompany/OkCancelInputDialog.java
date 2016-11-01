package home.cleaningcompany;

/**
 * Created by david on 11/1/2016.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.EditText;

public class OkCancelInputDialog {

    AlertDialog.Builder alert;
    String userInput = "";
    Resources resources;

    public String getUserInput()
    {
        return userInput;
    }

    public OkCancelInputDialog(Context context, String title, String message, String defaultInput) {
        alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        final EditText input = new EditText(context);
        input.setText(defaultInput);
        alert.setView(input);

        resources = context.getResources();

        alert.setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userInput = input.getText().toString();
                clickOk();

            }
        });

        alert.setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clickCancel();
            }
        });

    }

    public OkCancelInputDialog(Context context, String title, String message) {
        alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        final EditText input = new EditText(context);
        alert.setView(input);
        resources = context.getResources();



        alert.setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                userInput = input.getText().toString();
                clickOk();

            }
        });

        alert.setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clickCancel();
            }
        });


    }

    public void clickOk()
    {

    }

    public void clickCancel()
    {
    }


    public void show()
    {
        alert.show();
    }


}