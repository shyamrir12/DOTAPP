package com.example.awizom.dotapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.dotapp.Fragments.HandOverTelorList;
import com.example.awizom.dotapp.Models.HandOverModel;
import com.example.awizom.dotapp.PdfViewActivity;
import com.example.awizom.dotapp.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.factories.GreekAlphabetFactory.getString;


public class HandOverAdapter extends RecyclerView.Adapter<HandOverAdapter.OrderItemViewHolder> {

    private Context mCtx;
    ProgressDialog progressDialog;
    ImageButton img2, img3;
    private LinearLayout linearLayout;
    private String[] handoveritemList;
    TextView telor;
    //we are storing all the products in a list
    private List<HandOverModel> handoveritemlist;
    String message="";
    HandOverModel handover;
    String hTelor;
    private Intent pdfOpenintent;


    public HandOverAdapter(Context mCtx, List<HandOverModel> handoverlist, String hTelor) {
        this.mCtx = mCtx;
        this.handoveritemlist = handoverlist;
        this.hTelor = hTelor;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.handoverlist, null);
        return new OrderItemViewHolder(view, mCtx, handoveritemlist);

    }


    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        handover = handoveritemlist.get(position);
        try {
            // holder.telor.setText(handover.getTelorName());
            holder.snum.setText(handover.getSerialNo());
            holder.a_qty.setText(String.valueOf(handover.getAQty()));
            holder.catalog.setText(handover.getCatalogName());
            holder.design.setText(handover.getDesign());
            holder.page_no.setText(String.valueOf(handover.getPageNo()));
            holder.aunt.setText(handover.getUnit());

            //  holder.price.setText(String.valueOf( handover.getPrice()).toString());
            holder.price.setText(String.valueOf(handover.getPrice2()));
            // holder.sharedataButton.setOnClickListener(this);
            //  holder.serialNo.setText(String.valueOf(handover.getSerialNo()));


        } catch (Exception E) {
            E.printStackTrace();
        }





    }




    public static void shareApp(Context context,String message)
    {
//
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, message );
//        sendIntent.setType("text/plain");
//        context.startActivity(sendIntent);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(shareIntent, "SHARE"));

    }

    @Override
    public int getItemCount() {
        return handoveritemlist.size();
    }



    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        TextView a_qty, catalog, design, page_no, price, price2, telor, snum, aunt;
        ImageButton sharedataButton,print;

        //we are storing all the products in a list
        private List<HandOverModel> hndovritemList;


        public OrderItemViewHolder(View itemView, final Context mCtx, List<HandOverModel> handoveritemlist) {
            super(itemView);
            this.mCtx = mCtx;
            this.hndovritemList = handoveritemlist;

            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            //   telor = itemView.findViewById(R.id.tlr);
            snum = itemView.findViewById(R.id.snum);
            aunt = itemView.findViewById(R.id.unt3);
            a_qty = itemView.findViewById(R.id.qty);
            catalog = itemView.findViewById(R.id.catlogName);
            design = itemView.findViewById(R.id.design);
            page_no = itemView.findViewById(R.id.pgn);
            price = itemView.findViewById(R.id.princ);
            sharedataButton = itemView.findViewById(R.id.shareButton);
            print = itemView.findViewById(R.id.printButton);
            sharedataButton.setOnClickListener(this);
            print.setOnClickListener(this);

            //   price2 = itemView.findViewById(R.id.princ2);
            // receivedBy = itemView.findViewById(R.id.receivedby);


            //Button btn1 = itemView.findViewById(R.id.bton1);

        }


//        @Override
//        public void onClick(View v) {
//
//            int position = getAdapterPosition();
//            HandOverModel hndoitem = this.hndovritemList.get(position);
//            //createPdf(handoveritemlist.get(position).toString());
//
//
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.putExtra(Intent.EXTRA_TEXT, "Let's go for a trip to "
//                    + hndoitem);
//            intent.setType("text/plain");
//            mCtx.startActivity(Intent.createChooser(intent, "Send To"));
//        }


        private void createPdf(String sometext) {
            // create a new document
            PdfDocument document = new PdfDocument();
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawCircle(50, 50, 30, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(sometext, 80, 50, paint);
            //canvas.drawt
            // finish the page
            document.finishPage(page);
// draw text on the graphics object of the page
            // Create Page 2
            pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();
            paint = new Paint();
            paint.setColor(Color.BLUE);
            canvas.drawCircle(100, 100, 100, paint);
            document.finishPage(page);
            // write the document content
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
            File file = new File(directory_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + "test-2.pdf";
            File filePath = new File(targetPdf);
            try {
                document.writeTo(new FileOutputStream(filePath));
                //    Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("main", "error " + e.toString());
                //     Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
            }
            // close the document
            document.close();
        }

        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.shareButton:

                    String CatalogName="",Design="",SerialNo="",PageNo="",Unit="",qty="",Price="";

                    CatalogName=(handover.getCatalogName());
                    Design=(handover.getDesign());
                    SerialNo=(handover.getSerialNo());
                    PageNo= String.valueOf((handover.getPageNo()));
                    Unit=( handover.getUnit());
                    qty= String.valueOf((handover.getAQty()));
                    Price= String.valueOf((handover.getPrice()));
                    message =message+  hTelor+ "\nCatalog=" + CatalogName + "\nDesign=" + Design+"\nSerialNo="+SerialNo+ "\nPageNo=" + PageNo + "\nUnit=" + Unit+"\nQty="+qty;
                    shareApp(mCtx,message);

                    break;
                case R.id.printButton:
                    createPDF();
                    break;
            }
        }

        private void createPDF() {

            Document doc = new Document();

            PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2, 2});
            table.getDefaultCell().

                    setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell("Catalog Name");
            table.addCell("Design");
            table.addCell("SerialNo");
            table.addCell("PageNo");
            table.addCell("Unit");
            table.addCell("Aqty");
            // table.addCell("ReceivedBy");
            table.addCell("Price");
            //   table.addCell("Price");
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (
                    int j = 0;
                    j < cells.length; j++)

            {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }


            for (
                    int i = 0;
                    i < handoveritemlist.size(); i++)


            {

                table.addCell(handoveritemlist.get(i).getCatalogName().toString());
                table.addCell(handoveritemlist.get(i).getDesign().toString());
                table.addCell(handoveritemlist.get(i).getSerialNo().toString());
                table.addCell(String.valueOf(handoveritemlist.get(i).getPageNo()));
                table.addCell(handoveritemlist.get(i).getUnit().toString());
                table.addCell(String.valueOf(handoveritemlist.get(i).getAQty()));
                table.addCell(String.valueOf(handoveritemlist.get(i).getPrice2()));


            }

            try

            {
                // String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

                String path = mCtx.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()).getAbsolutePath();

                File dir = new File(String.valueOf(path));
                if (!dir.exists())
                    dir.mkdirs();

                Log.d("PDFCreator", "PDF Path: " + path);

                File file = new File(dir, "HandoverItemList.pdf");

                FileOutputStream fOut = new FileOutputStream(file);


                PdfWriter.getInstance(doc, fOut);

                //open the document
                doc.open();

                Paragraph p1 = new Paragraph(hTelor);


                /* You can also SET FONT and SIZE like this */
                Font paraFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.UNDERLINE, BaseColor.BLACK);
                p1.setAlignment(Paragraph.ALIGN_CENTER);

                p1.setSpacingAfter(20);
                p1.setFont(paraFont1);
                doc.add(p1);

                /* You can also SET FONT and SIZE like this */


                doc.setMargins(0, 0, 5, 5);
                doc.add(table);

                Phrase footerText = new Phrase("This is an example of a footer");
                HandOverAdapter.HeaderFooter pdfFooter = new HandOverAdapter.HeaderFooter();
                doc.newPage();

                Toast.makeText(mCtx, "Created...", Toast.LENGTH_LONG).show();


            } catch (
                    DocumentException de)

            {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (
                    IOException e)

            {
                Log.e("PDFCreator", "ioException:" + e);
            } finally

            {
                doc.close();
            }

            pdfOpenintent = new Intent();
            pdfOpenintent = new Intent(mCtx, PdfViewActivity.class);
            pdfOpenintent = pdfOpenintent.putExtra("PDFName","/ReceivedItemList.pdf");
            pdfOpenintent = pdfOpenintent.putExtra("PDFName","/HandoverItemList.pdf");
            mCtx.startActivity( pdfOpenintent);

        }

//        @Override
//        public boolean onLongClick(View v) {
//            int position = getAdapterPosition();
//            HandOverModel hndoitem = this.hndovritemList.get(position);
//
//
//            if (v.getId() == itemView.getId()) {
//                // showUpdateDeleteDialog(order);
//                try {
//
//                } catch (Exception E) {
//                    E.printStackTrace();
//                }
//                Toast.makeText(mCtx, "lc: ", Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
    }
    private class HeaderFooter {

        Phrase[] header = new Phrase[2];
        /**
         * Current page number (will be reset for every chapter).
         */
        int pagenumber;

        /**
         * Initialize one of the headers.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onOpenDocument(PdfWriter writer, Document document) {
            header[0] = new Phrase("Movie history");
        }

        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         * com.itextpdf.text.Paragraph)
         */
        public void onChapter(PdfWriter writer, Document document,
                              float paragraphPosition, Paragraph title) {
            header[1] = new Phrase(title.getContent());
            pagenumber = 1;
        }

        /**
         * Increase the page number.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }

        /**
         * Adds the header and the footer.
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            switch (writer.getPageNumber() % 2) {
                case 0:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_RIGHT, header[0],
                            rect.getRight(), rect.getTop(), 0);
                    break;
                case 1:
                    ColumnText.showTextAligned(writer.getDirectContent(),
                            Element.ALIGN_LEFT, header[1],
                            rect.getLeft(), rect.getTop(), 0);
                    break;
            }
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(String.format("page %d", pagenumber)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }
}
