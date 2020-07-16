package com.example.emailauth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    Context context;
    private File pdfFile;
Button btn,pbtn;
    ArrayList<UsersFire> MyList1;

    FirebaseUser user;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db;
    Task<DocumentSnapshot> documentReference;
    String uid,email;
    DatabaseReference reff;
    FirebaseAuth mAuth;
    List<String> id=new ArrayList<>();
    List<String> fname=new ArrayList<>();
  public  List<String> address=new ArrayList<>();
    public List<String> mail=new ArrayList<>();
    public List<String> mobile=new ArrayList<>();
    public static int l,m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn=findViewById(R.id.logoutbtn);
        pbtn=findViewById(R.id.pdfbtn);
        mAuth=FirebaseAuth.getInstance();
         firebaseDatabase = FirebaseDatabase.getInstance();
        db=FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = mAuth.getUid();
        mAuth=FirebaseAuth.getInstance();
        documentReference=db.collection("Customer").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(Home.this,documentSnapshot.get("fname").toString(),Toast.LENGTH_LONG).show();
                email=documentSnapshot.get("mail").toString();
                //method to of email
                sendemail();
            }
        });
        Task<QuerySnapshot> query = db.collection("Customer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    UsersFire user = documentSnapshot.toObject(UsersFire.class);
                    user.setId(documentSnapshot.getId());
                    id.add(documentSnapshot.getId());
                    fname.add(documentSnapshot.getString("fname"));
                    mail.add(documentSnapshot.getString("mail"));
                    address.add(documentSnapshot.getString("address"));
                    mobile.add(documentSnapshot.getString("mobile"));
                    l=id.size();
                }

            }
            });
       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mAuth.signOut();
               finish();
               startActivity(new Intent(Home.this, MainActivity.class));
           }
       });
       //below button is for pdf generation
       pbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   createPdfWrapper();
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (DocumentException e) {
                   e.printStackTrace();
               }
           }


       });

    }


//code to genrate pdf
    private void createPdfWrapper() throws FileNotFoundException, DocumentException {



            createPdf();

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.d("msg", "Created a new directory for PDF");
        }
        String pdfname = "GiftItem.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Id");
        table.addCell("FullName");
        table.addCell("Email");
        table.addCell("Address");
        table.addCell("Mobile");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }

        for(int i=0;i<id.size();i++)
        {
            table.addCell(id.get(i));
            table.addCell(fname.get(i));
            table.addCell(mail.get(i));
            table.addCell(address.get(i));
            table.addCell(mobile.get(i));
        }

//        System.out.println("Done");
        PdfWriter.getInstance(document, output);
        document.open();
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLUE);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLUE);
        document.add(new Paragraph("Report \n\n", f));
        document.add(table);
//        for (int i = 0; i < MyList1.size(); i++) {
//            document.add(new Paragraph(String.valueOf(MyList1.get(i))));
//        }
        document.close();
       // previewPdf();
    }
   /* private void previewPdf() {
        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }*/
   //code to call javaMailApi
   private void sendemail() {
       String mEmail = email;
       String mSubject = "";
       String mMessage = "You Just Logged In";


       JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);

       javaMailAPI.execute();
   }
}