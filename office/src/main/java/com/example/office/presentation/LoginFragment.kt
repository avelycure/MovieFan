package com.example.office.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.avelycure.core_navigation.IInstantiator
import com.example.office.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment() {
    companion object Instantiator : IInstantiator {
        private const val tag = "LoginFragment"

        override fun getTag(): String {
            return tag
        }

        override fun getInstance(bundle: Bundle): Fragment {
            val loginFragment = LoginFragment()
            loginFragment.arguments = bundle
            return loginFragment
        }
    }

    private lateinit var webView: WebView
    private val officeViewModel: OfficeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        return view
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.o_wv)
        webView.settings.allowFileAccess = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.d("mytag", "New url: $url")
                view?.loadUrl(url ?: "")
                return false
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d("mytag", "Headers: ${request?.requestHeaders}")
                return super.shouldOverrideUrlLoading(view, request)
            }


        }

        lifecycleScope.launchWhenCreated {
            officeViewModel.state.collect { state ->
                Log.d("mytag", state.token.requestToken)
                if (state.token.requestToken.isNotBlank())
                    webView.loadUrl("https://www.themoviedb.org/authenticate/" + state.token.requestToken +
                    //"?redirect_to=www.google.com")
                            "?redirect_to=http://www.google.com")
            }
        }
        officeViewModel.login()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.office_menu, menu)
        (activity as AppCompatActivity).supportActionBar?.title = "Office"
        super.onCreateOptionsMenu(menu, inflater)
    }
}