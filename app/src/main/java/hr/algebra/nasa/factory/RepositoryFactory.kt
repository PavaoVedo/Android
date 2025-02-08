package hr.algebra.nasa.factory

import android.content.Context
import hr.algebra.nasa.dao.AnimalSqlHelper

fun getNasaRepository(context: Context?) = AnimalSqlHelper(context)