package com.example.tutuproject.view.characterListScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutuproject.R
import com.example.tutuproject.databinding.FragmentCharacterListBinding
import com.example.tutuproject.others.ConnectionMode
import com.example.tutuproject.others.ConnectivityHelper
import com.example.tutuproject.others.RvItemDesigner
import com.example.tutuproject.others.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CharacterListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCharacterListBinding
    lateinit var viewModel: CharacterListViewModel
    private val TAG = "CharacterListFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(TAG, "onCreate: log working")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_character_list, container, false)
        binding = FragmentCharacterListBinding.bind(root)
        viewModel = ViewModelProvider(requireActivity())[CharacterListViewModel::class.java]

        val connectivityHelper = ConnectivityHelper(requireContext())
        connectivityHelper.networkAvailable.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                if (it == null) {
                    Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
                }
                if (it) {
                    viewModel.connectionMode.emit(ConnectionMode.Online)
                } else {
                    viewModel.connectionMode.emit(ConnectionMode.Offline)
                }
            }
        })

        setRv()

        setUiControl()

        return binding.root
    }

    private fun setRv() {
        val showRvLoader = MutableLiveData<Boolean>(false)

        val layoutManager = binding.charactersRv.layoutManager as LinearLayoutManager
        val rvAdapter = CharacterListRvAdapter(viewLifecycleOwner, showLoader = showRvLoader) { character ->
            // here will be the selected Character
            findNavController().navigate(R.id.characterFragment, Bundle().apply { putParcelable("character", character) })
        }
        binding.charactersRv.adapter = rvAdapter
        binding.charactersRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastCompletelyVisibleItemPosition =
                    layoutManager.findLastCompletelyVisibleItemPosition() - 1
                lifecycleScope.launch {
                    viewModel.lastVisibleItemPosition.emit(lastCompletelyVisibleItemPosition)
                }
            }
        })

        viewModel.characterList.collectFlow(viewLifecycleOwner) {
            if (it.isEmpty()) return@collectFlow
            Log.d(TAG, "setRv: ${it.size}")
            if (it.size == 20) {
                viewModel.saveToDb(it)
            }
            val newList = mutableListOf<RvItemDesigner>()
            newList.addAll(it.map { character ->  RvItemDesigner.CasualItem(character) })
            newList.add(RvItemDesigner.Loader)
            rvAdapter.submitList(newList.toList())
        }

        viewModel.action.collectFlow(viewLifecycleOwner) {
            when (it) {
                CharacterListScreenIntent.HideLoadingNextPage -> {
                    showRvLoader.postValue(false)
                }
                CharacterListScreenIntent.ShowLoadingNextPage -> {
                    showRvLoader.postValue(true)
                }
                CharacterListScreenIntent.ScrollRvTop -> {
                    binding.charactersRv.scrollToPosition(0)
                }
            }
        }
    }

    private fun setUiControl() {
        viewModel.screenStates.collectFlow(viewLifecycleOwner) {
            when (it) {
                CharacterListScreenStates.ErrorOccurredAndNothingToShow -> {
                    binding.apply {
                        charactersRv.visibility = View.GONE
                        errorOccuredLayout.root.visibility = View.VISIBLE
                        progressCircular.visibility = View.GONE
                        noDataFoundLayout.root.visibility = View.GONE
                    }
                }
                CharacterListScreenStates.HasDataToShow -> {
                    binding.apply {
                        charactersRv.visibility = View.VISIBLE
                        errorOccuredLayout.root.visibility = View.GONE
                        progressCircular.visibility = View.GONE
                        noDataFoundLayout.root.visibility = View.GONE
                    }
                }
                CharacterListScreenStates.Loading -> {
                    binding.apply {
                        charactersRv.visibility = View.GONE
                        errorOccuredLayout.root.visibility = View.GONE
                        progressCircular.visibility = View.VISIBLE
                        noDataFoundLayout.root.visibility = View.GONE
                    }
                }
                CharacterListScreenStates.NoDataToShow -> {
                    binding.apply {
                        charactersRv.visibility = View.GONE
                        errorOccuredLayout.root.visibility = View.GONE
                        progressCircular.visibility = View.GONE
                        noDataFoundLayout.root.visibility = View.VISIBLE
                    }
                }
                null -> {
                    binding.apply {
                        charactersRv.visibility = View.GONE
                        errorOccuredLayout.root.visibility = View.GONE
                        noDataFoundLayout.root.visibility = View.GONE
                        progressCircular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CharacterListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}