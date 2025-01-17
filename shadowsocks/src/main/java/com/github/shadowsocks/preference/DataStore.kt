/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.shadowsocks.preference

import android.os.Binder
import com.github.shadowsocks.Core
import com.github.shadowsocks.preference.PrefUtils.selectedServiceMode
import com.github.shadowsocks.utils.Key
import com.github.shadowsocks.utils.parsePort
import java.net.InetSocketAddress

object DataStore {
   /* val publicStore = RoomPreferenceDataStore(PublicDatabase.kvPairDao)*/
    // privateStore will only be used as temp storage for ProfileConfigFragment
    /*
        val privateStore = RoomPreferenceDataStore(PrivateDatabase.kvPairDao)
    */

  /*  init {
        publicStore.registerChangeListener(this)
    }*/



    // hopefully hashCode = mHandle doesn't change, currently this is true from KitKat to Nougat
    private val userIndex by lazy { Binder.getCallingUserHandle().hashCode() }
    private fun getLocalPort(key: String, default: Int): Int {
        return parsePort(/*ublicStore.getString(key)*/null, default + userIndex)
    }

    var profileId: Long =0
        /*get() = publicStore.getLong(Key.id) ?: 0
        set(value) = publicStore.putLong(Key.id, value)*/
    val persistAcrossReboot = false
        /*get() = publicStore.getBoolean(Key.persistAcrossReboot)
            ?: BootReceiver.enabled.also { publicStore.putBoolean(Key.persistAcrossReboot, it) }*/
    val canToggleLocked: Boolean get() =/* publicStore.getBoolean(Key.directBootAware) == true*/false
    val directBootAware: Boolean get() = Core.directBootSupported && canToggleLocked
    val serviceMode get() = /*publicStore.getString(Key.serviceMode) ?:*/ selectedServiceMode
    val listenAddress
        get() = /*if (publicStore.getBoolean(
                Key.shareOverLan,
                false
            )
        ) "0.0.0.0" else*/ "127.0.0.1"
    var portProxy: Int = getLocalPort(Key.portProxy, 1080)
        /*set(value) = publicStore.putString(Key.portProxy, value.toString())*/
    val proxyAddress get() = InetSocketAddress("127.0.0.1", portProxy)
    var portLocalDns: Int = getLocalPort(Key.portLocalDns, 5450)

    var portTransproxy: Int= getLocalPort(Key.portTransproxy, 8200)


    /**
     * Initialize settings that have complicated default values.
     */
  /*  fun initGlobal() {
        persistAcrossReboot
        if (publicStore.getString(Key.portProxy) == null) portProxy = portProxy
        if (publicStore.getString(Key.portLocalDns) == null) portLocalDns = portLocalDns
        if (publicStore.getString(Key.portTransproxy) == null) portTransproxy = portTransproxy
    }*/

    var editingId: Long? = null

    /*get() = privateStore.getLong(Key.id)
    set(value) = privateStore.putLong(Key.id, value)*/
    var proxyApps: Boolean = false

    /*get() = privateStore.getBoolean(Key.proxyApps) ?: false
    set(value) = privateStore.putBoolean(Key.proxyApps, value)*/
    var bypass: Boolean = false

    /*get() = privateStore.getBoolean(Key.bypass) ?: false
    set(value) = privateStore.putBoolean(Key.bypass, value)*/
    var individual: String = ""

    /* get() = privateStore.getString(Key.individual) ?: ""
     set(value) = privateStore.putString(Key.individual, value)*/
    var plugin: String = ""

    /*get() = privateStore.getString(Key.plugin) ?: ""
    set(value) = privateStore.putString(Key.plugin, value)*/
    var udpFallback: Long? = null

    /*get() = privateStore.getLong(Key.udpFallback)
    set(value) = privateStore.putLong(Key.udpFallback, value)*/
    var dirty: Boolean = false
    /*get() = privateStore.getBoolean(Key.dirty) ?: false
    set(value) = privateStore.putBoolean(Key.dirty, value)*/
}
