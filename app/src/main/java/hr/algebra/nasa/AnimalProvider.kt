package hr.algebra.nasa

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.nasa.dao.AnimalRepository
import hr.algebra.nasa.factory.getNasaRepository
import hr.algebra.nasa.model.Item

private const val AUTHORITY = "hr.algebra.nasa.api.provider"
private const val PATH = "items"
val NASA_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)){
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}


class NasaProvider : ContentProvider() {

    private lateinit var repository: AnimalRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.delete(selection, selectionArgs)
            // "hr.algebra.nasa.api.provider/items/55"
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.delete("${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(NASA_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getNasaRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? = repository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return repository.update(values, selection, selectionArgs)
            // "hr.algebra.nasa.api.provider/items/55"
            ITEM_ID -> uri.lastPathSegment?.let {
                return repository.update(values, "${Item::_id.name}=?", arrayOf(it))
            }
        }
        throw IllegalArgumentException("wrong uri")
    }
}