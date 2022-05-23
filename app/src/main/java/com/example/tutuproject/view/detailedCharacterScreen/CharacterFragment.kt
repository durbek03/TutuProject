package com.example.tutuproject.view.detailedCharacterScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tutuproject.R
import com.example.tutuproject.data.models.Character
import com.example.tutuproject.databinding.FragmentCharacterBinding
import com.example.tutuproject.databinding.FragmentCharacterListBinding
import com.example.tutuproject.useCases.GetCharacterListUseCase
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class CharacterFragment : Fragment() {
    lateinit var binding: FragmentCharacterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_character, container, false)
        binding = FragmentCharacterBinding.bind(root)
        val args = arguments?.getParcelable("character") as? Character
        if (args != null) {
            binding.charName.text = args.characterName
            Picasso.get().load(args.image).into(binding.image)
            binding.origin.text = "Origin: " + args.origin
            binding.status.text = "Status and Species: " + args.status + args.species
        }
        return binding.root
    }
}