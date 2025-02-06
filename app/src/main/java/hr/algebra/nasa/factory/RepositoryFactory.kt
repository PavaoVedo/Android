package hr.algebra.nasa.factory

import android.content.Context
import hr.algebra.nasa.dao.NasaSqlHelper

fun getNasaRepository(context: Context?) = NasaSqlHelper(context)