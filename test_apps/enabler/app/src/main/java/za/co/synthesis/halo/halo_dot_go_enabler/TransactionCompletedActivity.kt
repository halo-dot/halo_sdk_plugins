package za.co.synthesis.halo.halo_dot_go_enabler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class TransactionCompletedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_completed)

        val resultHeader = findViewById<TextView>(R.id.transaction_result_header_tv)
        val resultImage = findViewById<ImageView>(R.id.imageView2)
        val resultCentre = findViewById<TextView>(R.id.transaction_result_tv)
        val resultReason = findViewById<TextView>(R.id.transaction_result_reason_tv)

        val transactionResult = intent.getStringExtra("TransactionResult")
        val transactionId = intent.getStringExtra("TransactionId")
        val transactionType = intent.getStringExtra("TransactionType")

        val transactionDetailsButton = findViewById<Button>(R.id.transaction_details_btn)
        transactionDetailsButton.setOnClickListener {
            val intent = Intent(this, TransactionDetailsActivity::class.java)
            intent.putExtra("TransactionId", transactionId)
            intent.putExtra("TransactionType", transactionType)
            startActivity(intent)
        }

        if (transactionResult == "success") {
            resultHeader.text = resources.getString(R.string.transaction_successful)
            resultImage.setImageResource(R.drawable.img_link_generated)
            resultCentre.text = resources.getString(R.string.transaction_successful)
            resultReason.text = resources.getString(R.string.transaction_successful_reason)

            transactionDetailsButton.visibility = View.VISIBLE
        } else if (transactionResult != null && transactionResult != "") {
            resultHeader.text = resources.getString(R.string.transaction_unsuccessful)
            resultImage.setImageResource(R.drawable.transaction_unsuccessful)
            resultCentre.text = resources.getString(R.string.transaction_unsuccessful)
            resultReason.text = "$transactionResult"

            transactionDetailsButton.visibility = View.VISIBLE
        } else {
            resultHeader.text = resources.getString(R.string.transaction_unsuccessful)
            resultImage.setImageResource(R.drawable.transaction_unsuccessful)
            resultCentre.text = resources.getString(R.string.transaction_unsuccessful)
            resultReason.text = resources.getString(R.string.transaction_unsuccessful_reason)

            transactionDetailsButton.visibility = View.GONE
        }

        val doneButton = findViewById<Button>(R.id.done_btn)
        doneButton.setOnClickListener {
            finish()
        }
    }
}