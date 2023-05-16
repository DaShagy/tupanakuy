package com.dshagapps.tupanakuy.common.navigation

import androidx.navigation.*
import com.dshagapps.tupanakuy.common.navigation.subgraphs.AuthSubgraph.addAuthSubgraphNavigation
import com.dshagapps.tupanakuy.common.navigation.subgraphs.ClassroomSubgraph.addClassroomSubgraphNavigation
import com.dshagapps.tupanakuy.common.navigation.subgraphs.MainSubgraph.addMainSubgraphNavigation
import com.dshagapps.tupanakuy.common.navigation.subgraphs.SplashSubgraph.addSplashScreen

object AppNavGraph {
    fun NavGraphBuilder.addFeedScreenGraph(navController: NavController) {
        addSplashScreen(navController)
        addAuthSubgraphNavigation(navController)
        addMainSubgraphNavigation(navController)
        addClassroomSubgraphNavigation(navController)
    }
}