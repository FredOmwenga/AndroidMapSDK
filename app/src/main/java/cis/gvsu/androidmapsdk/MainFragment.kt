package cis.gvsu.androidmapsdk

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment() {
    var navCtrl : NavController? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        navCtrl = findNavController()
        setHasOptionsMenu(false)

        return inflater.inflate(R.layout.main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val richButton = view.findViewById(R.id.rich) as Button
        richButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 4)
            startActivityForResult(intent, 0)
        }

        val poorButton = view.findViewById(R.id.poor) as Button
        poorButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 1)
            startActivityForResult(intent, 1)
        }

        val inBetweenButton = view.findViewById(R.id.inBetween) as Button
        inBetweenButton.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            intent.putExtra("priceLimit", 42)
            startActivityForResult(intent, 0)
        }
    }
}