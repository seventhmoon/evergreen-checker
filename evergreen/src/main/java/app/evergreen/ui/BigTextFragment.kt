package app.evergreen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.evergreen.R

class BigTextFragment : DialogFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_big_text, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    arguments?.let { args ->
      if (args.containsKey(EXTRA_TITLE)) {
        view.findViewById<TextView>(R.id.big_text_title)?.text = args.getString(EXTRA_TITLE) ?: ""
      }
      if (args.containsKey(EXTRA_DESCRIPTION)) {
        view.findViewById<TextView>(R.id.big_text_description)?.text = args.getString(EXTRA_DESCRIPTION) ?: ""
      }
    }
  }

  companion object {
    const val TAG = "QrCodeFragment"

    private const val EXTRA_TITLE = "title"
    private const val EXTRA_DESCRIPTION = "description"

    fun withText(title: String, description: String) = BigTextFragment().apply {
      arguments = Bundle().apply {
        putString(EXTRA_TITLE, title)
        putString(EXTRA_DESCRIPTION, description)
      }
    }
  }
}

